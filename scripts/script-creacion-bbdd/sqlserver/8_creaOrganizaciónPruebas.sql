DECLARE @cod_organizacion decimal(1,0);
SET @cod_organizacion = 0;
DECLARE @nom_organizacion varchar(18);
SET @nom_organizacion= 'ENTORNO DE PRUEBAS';
DECLARE @cod_entidad decimal(1,0);
SET @cod_entidad= 1;
DECLARE @nom_entidad varchar(18);
SET @nom_entidad= 'ENTORNO DE PRUEBAS';
DECLARE @jndi varchar(25);
SET @jndi='sge_prueba_jndi';
DECLARE @directorio varchar(17);
SET @directorio='0/1';

-- Usuarios por defecto.

INSERT INTO A_USU ( USU_COD, USU_IDI, USU_APL, USU_LOG, USU_PAS, USU_NOM, USU_ACT, USU_CON, USU_LIS, USU_TEB, USU_TDM, USU_ICO, USU_MPR, USU_IGS, USU_IGN, USU_IPS, USU_IPN, USU_CBQ, USU_ICB, USU_NIF, USU_FIRMANTE, USU_EMAIL, USU_FIRMA, USU_TIPO_FIRMA, USU_BLQ ) 
	VALUES ( 5, 1, NULL, 'ADMIN', 'b521caa6e1db82e5a01c924a419870cb72b81635', 'ADMINISTRADOR', 1, 1, 1, 1, 1, 1, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL, NULL, 0) 
INSERT INTO A_USU ( USU_COD, USU_IDI, USU_APL, USU_LOG, USU_PAS, USU_NOM, USU_ACT, USU_CON, USU_LIS, USU_TEB, USU_TDM, USU_ICO, USU_MPR, USU_IGS, USU_IGN, USU_IPS, USU_IPN, USU_CBQ, USU_ICB, USU_NIF, USU_FIRMANTE, USU_EMAIL, USU_FIRMA, USU_TIPO_FIRMA, USU_BLQ ) 
	VALUES ( 4, 1, NULL, 'SW.FORMULARIOS', 'a02f2e7643380d66b8506e3a3ea96e23b289641e', 'Usuario No Registrado', 1, 1, 1, 1, 1, 1, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, NULL, NULL, NULL, 0) 
INSERT INTO A_USU_ROTACION_PASS (COD_PASS,USU_PASS, FEC_ALTA, USU_COD) VALUES (1,'b521caa6e1db82e5a01c924a419870cb72b81635',getdate(),5);


-- Organizaciones por defecto
INSERT INTO A_ORG ( ORG_COD, ORG_DES, ORG_MUN, ORG_ICO, ORG_NUM, ORG_CSS ) VALUES (@cod_organizacion, @nom_organizacion, NULL, '/images/org/logo.jpg', 1, 1); 


-- Entidades por defecto
INSERT INTO A_ENT ( ENT_ORG, ENT_COD, ENT_NOM, ENT_DTR, ENT_TIP ) VALUES (@cod_organizacion, @cod_entidad, @nom_entidad, @directorio, 'A'); 

-- Permisos para usuario ADMIN por aplicación
INSERT INTO A_AAU (AAU_APL,AAU_USU,AAU_ORG,AAU_ENT) SELECT APL_COD, 5,0,1 FROM A_APL;
INSERT INTO A_AAE (AAE_APL, AAE_ORG, AAE_ENT, AAE_LIC) SELECT APL_COD, 0, 1, NULL FROM A_APL;
INSERT INTO A_EEA (EEA_APL,AAE_ORG,EEA_ENT,EEA_EJE,EEA_EST,EEA_BDE,EEA_VBD,EEA_UBD,EEA_PWD,EEA_DBMS,EEA_ARQ,EEA_LOG,
	EEA_TRA,EEA_QTO,EEA_PWDBD,EEA_DB,EEA_ODBC) SELECT APL_COD, @cod_organizacion, @cod_entidad, 0, '1', @jndi, '1', null,null,null,null,null,null,null,null,null,null FROM A_APL;
INSERT INTO A_UAE (UAE_USU, UAE_ORG, UAE_ENT, UAE_APL, UAE_EDE) SELECT 5, @cod_organizacion, @cod_entidad, APL_COD, NULL FROM A_APL;
INSERT INTO A_UGO (UGO_USU, UGO_ORG, UGO_ENT, UGO_GRU, UGO_APL) SELECT 5, @cod_organizacion, @cod_entidad, 1, APL_COD FROM A_APL;
INSERT INTO A_AID (AID_APL,AID_IDI,AID_TEX) SELECT APL_COD, 1, APL_NOM FROM A_APL;
INSERT INTO A_OUS (OUS_ORG, OUS_USU) VALUES (@cod_organizacion, 5);

-- Menús
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,1,1,'Menú completo registro',1);
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,3,1,'Menú completo terceros',1);
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,4,1,'Menú Completo Expedientes',1);
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,5,1,'Menú administracion',1);
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,7,1,'Menú Web Ciudadano',1);
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,9,1,'Menú administrador local',1);
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,11,1,'Portafirmas',1);
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,15,1,'Gestión de formularios',1);
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,16,1,'Gestión de informes',1);
Insert into A_MNU (MNU_ORG,MNU_APL,MNU_COD,MNU_DES,MNU_ACT) values (@cod_organizacion,18,1,'Usuarios de formularios',1);

Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,1,1,1,'Registro de Entrada',0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,2,2,1,'Registro de Salida',0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,3,3,1,'Mantenimientos',0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,12,12,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,13,13,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,15,15,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,16,16,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,17,17,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,22,22,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,23,23,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,25,25,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,26,26,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,27,27,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,31,31,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,32,32,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,33,33,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,34,34,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,35,35,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,131,131,1,NULL,13);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,132,132,1,NULL,13);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,151,151,1,NULL,15);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,152,152,1,NULL,15);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,153,153,1,NULL,15);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,231,231,1,NULL,23);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,232,232,1,NULL,23);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,251,251,1,NULL,25);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,252,252,1,NULL,25);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,253,253,1,NULL,25);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,254,254,1,NULL,25);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,255,254,1,NULL,15);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,256,255,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,257,256,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,258,260,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,259,263,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,260,262,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,261,261,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,262,265,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,266,266,1,NULL,13);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,267,268,1,NULL,3);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,268,267,1,NULL,3);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,1,1,269,15,269,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,1,1,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,2,2,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,4,4,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,24,24,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,26,26,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,27,27,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,29,29,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,30,30,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,31,31,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,40,40,1,NULL,4);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,41,41,1,NULL,4);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,42,42,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,45,45,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,47,47,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,48,48,1,NULL,4);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,49,49,1,NULL,4);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,50,50,1,NULL,4);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,51,44,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,52,51,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,53,52,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,54,53,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,55,54,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,3,1,56,55,1,NULL,2);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,70,0,1,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,71,70,11,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,72,70,12,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,73,0,57,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,74,73,63,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,75,73,50,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,76,73,62,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,77,73,61,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,78,74,64,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,79,75,48,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,80,75,49,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,81,75,47,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,82,0,65,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,83,82,59,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,84,82,60,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,85,0,66,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,86,85,3,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,87,85,67,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,88,87,68,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,4,1,89,87,69,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,1,1,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,2,10,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,3,11,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,4,13,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,5,14,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,6,17,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,7,18,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,8,19,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,9,33,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,10,38,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,11,39,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,12,40,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,13,41,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,14,42,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,15,43,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,16,44,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,17,45,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,18,46,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,19,47,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,20,2,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,21,22,1,NULL,20);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,22,23,1,NULL,20);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,23,24,1,NULL,20);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,24,20,1,NULL,20);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,25,48,1,NULL,20);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,26,16,1,NULL,20);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,30,49,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,31,50,1,NULL,20);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,32,52,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,33,51,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,34,35,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,35,36,1,NULL,34);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,36,37,1,NULL,34);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,37,60,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,38,61,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,5,1,39,62,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,7,1,1,1,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,7,1,2,13,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,1,0,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,2,1,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,8,7,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,12,11,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,14,4,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,15,5,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,17,2,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,18,8,1,NULL,17);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,20,13,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,21,14,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,23,17,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,24,20,1,NULL,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,25,12,1,NULL,17);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,9,1,26,21,1,NULL,1);
INSERT INTO A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PAD,MOR_PRO,MOR_VIS) VALUES (@cod_organizacion,9,1,27,17,15,1);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,11,1,1,1,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,11,1,2,2,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,15,1,1,4,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,15,1,2,5,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,15,1,3,6,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,15,1,4,7,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,16,1,1,0,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,16,1,2,1,1,NULL,0);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,16,1,3,2,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,16,1,4,3,1,NULL,2);
Insert into A_MOR (MOR_ORG,MOR_APL,MOR_MNU,MOR_ELE,MOR_PRO,MOR_VIS,MOR_DES,MOR_PAD) values (@cod_organizacion,1,1,(select max(mor_ele) +1 from a_mor where mor_apl=1),270,1,null,3); 

-- Permisos procesos / menús 

Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 1, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=1;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 3, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=3;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 4, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=4;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 5, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=5;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 6, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=6;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 7, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=7;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 9, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=9;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 11, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=11;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 15, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=15;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 16, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=16;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 17, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=17;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 18, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=18;
Insert into A_RPG (RPG_GRU,RPG_ORG,RPG_ENT,RPG_APL,RPG_PRO,RPG_TIP) SELECT 1, @cod_organizacion, @cod_entidad, 19, PRO_COD, 1 FROM A_PRO WHERE PRO_APL=19;
GO
