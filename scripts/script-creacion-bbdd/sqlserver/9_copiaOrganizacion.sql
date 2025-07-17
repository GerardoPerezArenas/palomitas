DECLARE @cod_organizacion_real decimal(3,0);
SET @cod_organizacion_real = 19;
DECLARE @nom_organizacion_real varchar(19);
SET @nom_organizacion_real = 'DIPUTACIÓN DE ÁVILA';
DECLARE @cod_entidad_real decimal(1,0);
SET @cod_entidad_real= 1;
DECLARE @nom_entidad_real varchar(18);
SET @nom_entidad_real= 'DIPUTACIÓN DE ÁVILA';
DECLARE @jndi varchar(17);
SET @jndi='sge_real_jndi';
DECLARE @directorio varchar(17);
SET @directorio='19/1';

-- Organizaciones por defecto
INSERT INTO A_ORG ( ORG_COD, ORG_DES, ORG_MUN, ORG_ICO, ORG_NUM, ORG_CSS ) VALUES (@cod_organizacion_real, @nom_organizacion_real, NULL, '/images/org/logo.jpg', 1, 1); 


-- Entidades por defecto
INSERT INTO A_ENT ( ENT_ORG, ENT_COD, ENT_NOM, ENT_DTR, ENT_TIP ) VALUES (@cod_organizacion_real, @cod_entidad_real, @nom_entidad_real, @directorio, 'A'); 

INSERT INTO A_AAE (AAE_APL, AAE_ORG, AAE_ENT, AAE_LIC) SELECT APL_COD, @cod_organizacion_real, 1, NULL FROM A_APL;
INSERT INTO A_EEA (EEA_APL,AAE_ORG,EEA_ENT,EEA_EJE,EEA_EST,EEA_BDE,EEA_VBD,EEA_UBD,EEA_PWD,EEA_DBMS,EEA_ARQ,EEA_LOG,
	EEA_TRA,EEA_QTO,EEA_PWDBD,EEA_DB,EEA_ODBC) SELECT APL_COD, @cod_organizacion_real, @cod_entidad_real, 0, '1', @jndi, '1', null,null,null,null,null,null,null,null,null,null FROM A_APL;


INSERT INTO A_MNU(MNU_ORG, MNU_APL, MNU_COD, MNU_DES, MNU_ACT) (SELECT @cod_organizacion_real, MNU_APL, MNU_COD, MNU_DES, MNU_ACT FROM A_MNU WHERE MNU_ORG = 0); 
INSERT INTO A_MOR(MOR_ORG, MOR_APL, MOR_MNU, MOR_ELE, MOR_PRO, MOR_VIS, MOR_DES, MOR_PAD) (SELECT @cod_organizacion_real, MOR_APL, MOR_MNU, MOR_ELE, MOR_PRO, MOR_VIS, MOR_DES, MOR_PAD FROM A_MOR WHERE MOR_ORG = 0);
INSERT INTO A_RPG(RPG_GRU, RPG_ORG, RPG_ENT, RPG_APL, RPG_PRO, RPG_TIP) (SELECT RPG_GRU, @cod_organizacion_real, ENT_COD, RPG_APL, RPG_PRO, RPG_TIP FROM A_RPG JOIN A_ENT ON (@cod_organizacion_real = ENT_ORG) WHERE RPG_ORG = 0 AND RPG_GRU = 1);
GO
