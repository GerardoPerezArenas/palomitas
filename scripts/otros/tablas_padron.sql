-- Padron de habitantes (Revision para eliminarlas)

CREATE TABLE P_PRM (	
	PRM_COD NUMBER(3,0) NOT NULL, 
	PRM_TID NUMBER(2,0), 
	PRM_CIN NUMBER(2,0), 
	PRM_CCO NUMBER(2,0), 
	PRM_TIT VARCHAR2(2), 
	PRM_CAL NUMBER(3,0), 
	PRM_CSE NUMBER(3,0), 
	PRM_FAM NUMBER(1,0) NOT NULL, 
	PRM_SOP NUMBER(1,0), 
	PRM_SEX VARCHAR2(1) NOT NULL, 
	PRM_MZN NUMBER(1,0) NOT NULL, 
	PRM_PAI NUMBER(3,0) NOT NULL, 
	PRM_PRV NUMBER(2,0) NOT NULL, 
	PRM_MUN NUMBER(3,0) NOT NULL, 
	CONSTRAINT PK_P_PRM PRIMARY KEY (PRM_COD), 
	CONSTRAINT FK_P_PRM_REL_PH_11_T_TID FOREIGN KEY (PRM_TID)
		REFERENCES T_TID (TID_COD)
);

COMMENT ON TABLE P_PRM IS 'PARAMETROS PMH';
COMMENT ON COLUMN P_PRM.PRM_COD IS 'Código de parametrización';
COMMENT ON COLUMN P_PRM.PRM_TID IS 'Código tipo identificador';
COMMENT ON COLUMN P_PRM.PRM_CIN IS 'Certificado individual por defecto';
COMMENT ON COLUMN P_PRM.PRM_CCO IS 'Certificado colectivo por defecto';
COMMENT ON COLUMN P_PRM.PRM_TIT IS 'Código de titulación';
COMMENT ON COLUMN P_PRM.PRM_CAL IS 'Código del cargo que firma como alcalde';
COMMENT ON COLUMN P_PRM.PRM_CSE IS 'Código del cargo que firma como secretario';
COMMENT ON COLUMN P_PRM.PRM_FAM IS 'Permitir la gestión de familias';
COMMENT ON COLUMN P_PRM.PRM_SOP IS 'Trabajar obligatoriamente con suboperaciones';
COMMENT ON COLUMN P_PRM.PRM_SEX IS 'Sistema intergado con seguimiento expedientes';
COMMENT ON COLUMN P_PRM.PRM_MZN IS 'Mostrar manzana en hojas';
COMMENT ON COLUMN P_PRM.PRM_PAI IS 'Código de país';
COMMENT ON COLUMN P_PRM.PRM_PRV IS 'Código de provincia';
COMMENT ON COLUMN P_PRM.PRM_MUN IS 'Código del municipio';

CREATE INDEX REL_PH_10_FK ON P_PRM (PRM_CAL);
CREATE INDEX REL_PH_11_FK ON P_PRM (PRM_TID);
CREATE INDEX REL_PH_16_FK ON P_PRM (PRM_TIT);
CREATE INDEX REL_PH_51_FK ON P_PRM (PRM_PAI, PRM_PRV, PRM_MUN);
CREATE INDEX REL_PH_52_FK ON P_PRM (PRM_CCO);
CREATE INDEX REL_PH_53_FK ON P_PRM (PRM_CSE);
CREATE INDEX REL_PH_6_FK ON P_PRM (PRM_CIN);


CREATE TABLE P_HOJ (	
	HOJ_PAI NUMBER(3,0) NOT NULL, 
	HOJ_PRV NUMBER(2,0) NOT NULL, 
	HOJ_MUN NUMBER(3,0) NOT NULL, 
	HOJ_DIS NUMBER(2,0) NOT NULL, 
	HOJ_SEC NUMBER(3,0) NOT NULL, 
	HOJ_NUM NUMBER(4,0) NOT NULL, 
	HOJ_FAM NUMBER(2,0) NOT NULL, 
	HOJ_VER NUMBER(3,0) NOT NULL, 
	HOJ_DOM NUMBER(12,0) NOT NULL, 
	HOJ_URB VARCHAR2(15), 
	HOJ_CON NUMBER(3,0) NOT NULL, 
	HOJ_SIT VARCHAR2(1) NOT NULL, 
	HOJ_LET VARCHAR2(1) NOT NULL, 
	CONSTRAINT PK_P_HOJ PRIMARY KEY (HOJ_NUM, HOJ_PAI, HOJ_PRV, HOJ_MUN, HOJ_DIS, HOJ_SEC, HOJ_LET, HOJ_FAM, HOJ_VER), 
	CONSTRAINT FK_P_HOJ_REL_PH_1_T_SEC FOREIGN KEY (HOJ_PAI, HOJ_PRV, HOJ_MUN, HOJ_DIS, HOJ_SEC, HOJ_LET)
		REFERENCES T_SEC (SEC_PAI, SEC_PRV, SEC_MUN, SEC_DIS, SEC_COD, SEC_LET), 
	CONSTRAINT FK_P_HOJ_REL_PH_2_T_DPO FOREIGN KEY (HOJ_DOM)
		REFERENCES T_DPO (DPO_DOM)
);

COMMENT ON TABLE P_HOJ IS 'HOJAS PADRONALES';
COMMENT ON COLUMN P_HOJ.HOJ_SIT IS 'Situación';
COMMENT ON COLUMN P_HOJ.HOJ_LET IS 'Letra de seccion';
COMMENT ON COLUMN P_HOJ.HOJ_PAI IS 'Código de país';
COMMENT ON COLUMN P_HOJ.HOJ_PRV IS 'Código de provincia';
COMMENT ON COLUMN P_HOJ.HOJ_MUN IS 'Código del municipio';
COMMENT ON COLUMN P_HOJ.HOJ_DIS IS 'Código de distrito';
COMMENT ON COLUMN P_HOJ.HOJ_SEC IS 'Código de sección';
COMMENT ON COLUMN P_HOJ.HOJ_NUM IS 'Número de hoja padronal';
COMMENT ON COLUMN P_HOJ.HOJ_FAM IS 'Número de familia';
COMMENT ON COLUMN P_HOJ.HOJ_VER IS 'Número de modificación';
COMMENT ON COLUMN P_HOJ.HOJ_DOM IS 'Código de domicilio';
COMMENT ON COLUMN P_HOJ.HOJ_URB IS 'Urbanización';
COMMENT ON COLUMN P_HOJ.HOJ_CON IS 'Contador de habitantes en la hoja';

CREATE INDEX REL_PH_1_FK ON  P_HOJ (HOJ_PAI, HOJ_PRV, HOJ_MUN, HOJ_DIS, HOJ_SEC, HOJ_LET);
CREATE INDEX REL_PH_2_FK ON  P_HOJ (HOJ_DOM);


CREATE TABLE P_TIT (	
	TIT_COD VARCHAR2(2) NOT NULL, 
	TIT_DES VARCHAR2(200) NOT NULL, 
	CONSTRAINT PK_P_TIT PRIMARY KEY (TIT_COD)
);

COMMENT ON COLUMN P_TIT.TIT_COD IS 'Código de titulación';
COMMENT ON COLUMN P_TIT.TIT_DES IS 'Descripción';
COMMENT ON TABLE P_TIT IS 'TITULACIONES';


CREATE TABLE P_HAB (	
	HAB_COD NUMBER(9,0) NOT NULL, 
	HAB_PAI NUMBER(3,0) NOT NULL, 
	HAB_PRV NUMBER(2,0) NOT NULL, 
	HAB_MUN NUMBER(3,0) NOT NULL, 
	HAB_DIS NUMBER(2,0) NOT NULL, 
	HAB_SEC NUMBER(3,0) NOT NULL, 
	HAB_HOJ NUMBER(4,0) NOT NULL, 
	HAB_FAM NUMBER(2,0) NOT NULL, 
	HAB_VER NUMBER(3,0), 
	HAB_OPE NUMBER(9,0), 
	HAB_TID NUMBER(4,0) NOT NULL, 
	HAB_DOC VARCHAR2(25), 
	HAB_NOM VARCHAR2(20) NOT NULL, 
	HAB_AP1 VARCHAR2(25) NOT NULL, 
	HAB_PA1 VARCHAR2(10), 
	HAB_AP2 VARCHAR2(25), 
	HAB_PA2 VARCHAR2(10), 
	HAB_ORD NUMBER(2,0) NOT NULL, 
	HAB_TEL VARCHAR2(40), 
	HAB_SEX VARCHAR2(1) NOT NULL, 
	HAB_FNA DATE NOT NULL, 
	HAB_FEM DATE, 
	HAB_NIE VARCHAR2(11), 
	HAB_SIT VARCHAR2(1) NOT NULL, 
	HAB_NIA VARCHAR2(15), 
	HAB_FAL DATE NOT NULL, 
	HAB_UAL NUMBER(4,0) NOT NULL, 
	HAB_FBA DATE, 
	HAB_UBA NUMBER(4,0), 
	HAB_PNA NUMBER(3,0), 
	HAB_TIT VARCHAR2(2), 
	HAB_PAN NUMBER(3,0), 
	HAB_PRN NUMBER(2,0), 
	HAB_MNA NUMBER(3,0), 
	HAB_NOF VARCHAR2(100), 
	HAB_FAR NUMBER(2,0), 
	HAB_LET VARCHAR2(1) NOT NULL, 
	CONSTRAINT PK_P_HAB PRIMARY KEY (HAB_COD), 
	CONSTRAINT FK_P_HAB_REL_PH_48_T_TID FOREIGN KEY (HAB_TID)
		REFERENCES T_TID (TID_COD), 
	CONSTRAINT FK_P_HAB_REL_PH_18_P_HOJ FOREIGN KEY (HAB_HOJ, HAB_PAI, HAB_PRV, HAB_MUN, HAB_DIS, HAB_SEC, HAB_LET, HAB_FAM, HAB_VER)
		REFERENCES P_HOJ (HOJ_NUM, HOJ_PAI, HOJ_PRV, HOJ_MUN, HOJ_DIS, HOJ_SEC, HOJ_LET, HOJ_FAM, HOJ_VER), 
	CONSTRAINT FK_P_HAB_REL_PH_56_P_TIT FOREIGN KEY (HAB_TIT)
		REFERENCES P_TIT (TIT_COD)
);

COMMENT ON TABLE P_HAB IS 'HABITANTES';
COMMENT ON COLUMN P_HAB.HAB_COD IS 'Código de habitante';
COMMENT ON COLUMN P_HAB.HAB_PAI IS 'Código de país hoja padronal';
COMMENT ON COLUMN P_HAB.HAB_PRV IS 'Código de provincia hoja padronal';
COMMENT ON COLUMN P_HAB.HAB_MUN IS 'Código del municipio hoja padronal';
COMMENT ON COLUMN P_HAB.HAB_DIS IS 'Código de distrito hoja padronal';
COMMENT ON COLUMN P_HAB.HAB_SEC IS 'Código de sección hoja padronal';
COMMENT ON COLUMN P_HAB.HAB_HOJ IS 'Número de hoja padronal';
COMMENT ON COLUMN P_HAB.HAB_FAM IS 'Número de familia';
COMMENT ON COLUMN P_HAB.HAB_VER IS 'Número de modificación';
COMMENT ON COLUMN P_HAB.HAB_OPE IS 'Número de operación';
COMMENT ON COLUMN P_HAB.HAB_DOC IS 'Documento';
COMMENT ON COLUMN P_HAB.HAB_NOM IS 'Nombre';
COMMENT ON COLUMN P_HAB.HAB_AP1 IS '1er Apellido';
COMMENT ON COLUMN P_HAB.HAB_PA1 IS 'Partícula 1er Apellido';
COMMENT ON COLUMN P_HAB.HAB_AP2 IS '2do Apellido';
COMMENT ON COLUMN P_HAB.HAB_PA2 IS 'Partícula 2do. Apellido';
COMMENT ON COLUMN P_HAB.HAB_ORD IS 'Orden del habitante en la hoja padronal';
COMMENT ON COLUMN P_HAB.HAB_TEL IS 'Tel¿fono';
COMMENT ON COLUMN P_HAB.HAB_SEX IS 'Sexo';
COMMENT ON COLUMN P_HAB.HAB_FNA IS 'Fecha de nacimiento';
COMMENT ON COLUMN P_HAB.HAB_FEM IS 'Fecha de empadronamiento';
COMMENT ON COLUMN P_HAB.HAB_NIE IS 'Número de identificación electoral';
COMMENT ON COLUMN P_HAB.HAB_SIT IS 'Situación';
COMMENT ON COLUMN P_HAB.HAB_NIA IS 'Número de identificación del ayuntamiento';
COMMENT ON COLUMN P_HAB.HAB_FAL IS 'Fecha y hora de alta';
COMMENT ON COLUMN P_HAB.HAB_UAL IS 'Usuario alta';
COMMENT ON COLUMN P_HAB.HAB_FBA IS 'Fecha de baja';
COMMENT ON COLUMN P_HAB.HAB_UBA IS 'Usuario baja';

CREATE INDEX REL_PH_18_FK ON P_HAB (HAB_PAI, HAB_PRV, HAB_MUN, HAB_DIS, HAB_SEC, HAB_LET, HAB_HOJ, HAB_FAM, HAB_VER);
CREATE INDEX REL_PH_21_FK2 ON P_HAB (HAB_OPE);
CREATE INDEX REL_PH_48_FK ON P_HAB (HAB_TID);
CREATE INDEX REL_PH_55_FK ON P_HAB (HAB_PNA); 
CREATE INDEX REL_PH_56_FK ON P_HAB (HAB_TIT);
CREATE INDEX REL_PH_57_FK ON P_HAB (HAB_PAN, HAB_PRN, HAB_MNA);


CREATE TABLE P_CON (	
	CON_COD NUMBER(3,0) NOT NULL, 
	CON_NOM VARCHAR2(50) NOT NULL, 
	CONSTRAINT PK_P_CON PRIMARY KEY (CON_COD)
);

COMMENT ON COLUMN P_CON.CON_COD IS 'Código de consulado';
COMMENT ON COLUMN P_CON.CON_NOM IS 'Nombre';
COMMENT ON TABLE P_CON IS 'CONSULADOS';


CREATE TABLE P_TOP (	
	TOP_DES VARCHAR2(60) NOT NULL, 
	TOP_COV VARCHAR2(1) NOT NULL, 
	TOP_CAV VARCHAR2(2) NOT NULL, 
	CONSTRAINT PK_P_TOP PRIMARY KEY (TOP_COV, TOP_CAV)
);
  
COMMENT ON COLUMN P_TOP.TOP_DES IS 'Descripción';
COMMENT ON TABLE P_TOP IS 'TIPOS DE OPERACIONES';


CREATE TABLE  P_HOP (	
	HOP_OPE NUMBER(9,0) NOT NULL, 
	HOP_VER NUMBER(3,0) NOT NULL, 
	HOP_PAI NUMBER(3,0) NOT NULL, 
	HOP_PRV NUMBER(2,0) NOT NULL, 
	HOP_MUN NUMBER(3,0) NOT NULL, 
	HOP_DIS NUMBER(2,0) NOT NULL, 
	HOP_SEC NUMBER(4,0) NOT NULL, 
	HOP_HOJ NUMBER(4,0) NOT NULL, 
	HOP_FAM NUMBER(2,0) NOT NULL, 
	HOP_VERH NUMBER(3,0) NOT NULL, 
	HOP_TIT VARCHAR2(2), 
	HOP_PAN NUMBER(3,0), 
	HOP_PANC NUMBER(3,0), 
	HOP_PRNC NUMBER(2,0), 
	HOP_MUNC NUMBER(3,0), 
	HOP_PAO NUMBER(3,0), 
	HOP_PRO NUMBER(2,0), 
	HOP_MUO NUMBER(3,0), 
	HOP_PAD NUMBER(3,0), 
	HOP_PRD NUMBER(2,0), 
	HOP_MUD NUMBER(3,0), 
	HOP_CONO NUMBER(3,0), 
	HOP_OPEA NUMBER(9,0), 
	HOP_FEC DATE NOT NULL, 
	HOP_FOP DATE NOT NULL, 
	HOP_HAB NUMBER(9,0), 
	HOP_TID NUMBER(4,0) NOT NULL, 
	HOP_DOC VARCHAR2(25), 
	HOP_NOM VARCHAR2(20) NOT NULL, 
	HOP_AP1 VARCHAR2(25) NOT NULL, 
	HOP_PA1 VARCHAR2(10), 
	HOP_AP2 VARCHAR2(25), 
	HOP_PA2 VARCHAR2(10), 
	HOP_ORD NUMBER(2,0) NOT NULL, 
	HOP_TEL VARCHAR2(12), 
	HOP_SEX VARCHAR2(1) NOT NULL, 
	HOP_FNA DATE NOT NULL, 
	HOP_FEM DATE, 
	HOP_NIE VARCHAR2(11), 
	HOP_SIT VARCHAR2(1) NOT NULL, 
	HOP_NIA VARCHAR2(15), 
	HOP_COV VARCHAR2(1) NOT NULL, 
	HOP_CAV VARCHAR2(2) NOT NULL, 
	HOP_COD NUMBER(3,0), 
	HOP_LET VARCHAR2(1) NOT NULL, 
	HOP_NOF VARCHAR2(100), 
	HOP_ACC CHAR(1) DEFAULT 2 NOT NULL, 
	HOP_FAC DATE, 
	CONSTRAINT PK_P_HOP PRIMARY KEY (HOP_OPE, HOP_VER), 
	CONSTRAINT FK_P_HOP_REL_PH_50_T_TID FOREIGN KEY (HOP_TID)
		REFERENCES  T_TID (TID_COD), 
	CONSTRAINT FK_P_HOP_REL_PH_28_P_CON FOREIGN KEY (HOP_CONO)
		REFERENCES  P_CON (CON_COD), 
	CONSTRAINT FK_P_HOP_REL_PH_29_P_CON FOREIGN KEY (HOP_COD)
		REFERENCES  P_CON (CON_COD), 
	CONSTRAINT FK_P_HOP_REL_PH_27_P_HAB FOREIGN KEY (HOP_HAB)
		REFERENCES  P_HAB (HAB_COD), 
	CONSTRAINT FK_P_HOP_REL_PH_4_P_HOJ FOREIGN KEY (HOP_HOJ, HOP_PAI, HOP_PRV, HOP_MUN, HOP_DIS, HOP_SEC, HOP_LET, HOP_FAM, HOP_VERH)
		REFERENCES  P_HOJ (HOJ_NUM, HOJ_PAI, HOJ_PRV, HOJ_MUN, HOJ_DIS, HOJ_SEC, HOJ_LET, HOJ_FAM, HOJ_VER), 
	CONSTRAINT FK_P_HOP_REL_PH_42_P_TIT FOREIGN KEY (HOP_TIT)
		REFERENCES  P_TIT (TIT_COD), 
	CONSTRAINT FK_P_HOP_REL_PH_31_P_TOP FOREIGN KEY (HOP_COV, HOP_CAV)
		REFERENCES  P_TOP (TOP_COV, TOP_CAV)
);

COMMENT ON TABLE P_HOP IS 'HISTORICO DE OPERACIONES';
COMMENT ON COLUMN P_HOP.HOP_OPE IS 'Número de operación';
COMMENT ON COLUMN P_HOP.HOP_VER IS 'Número de modificación2';
COMMENT ON COLUMN P_HOP.HOP_PAI IS 'País hoja padronal';
COMMENT ON COLUMN P_HOP.HOP_PRV IS 'Provincia hoja padronal';
COMMENT ON COLUMN P_HOP.HOP_MUN IS 'Municipio hoja padronal';
COMMENT ON COLUMN P_HOP.HOP_DIS IS 'Distrito hoja padronal';
COMMENT ON COLUMN P_HOP.HOP_SEC IS 'Sección hoja padronal';
COMMENT ON COLUMN P_HOP.HOP_HOJ IS 'Número de hoja padronal';
COMMENT ON COLUMN P_HOP.HOP_FAM IS 'Número de familia';
COMMENT ON COLUMN P_HOP.HOP_VERH IS 'Número de modificación de la hoja padronal';
COMMENT ON COLUMN P_HOP.HOP_TIT IS 'Código de titulación del habitante';
COMMENT ON COLUMN P_HOP.HOP_PAN IS 'País de nacionalidad';
COMMENT ON COLUMN P_HOP.HOP_PANC IS 'País municipio nacimiento';
COMMENT ON COLUMN P_HOP.HOP_PRNC IS 'Provincia municipio nacimiento';
COMMENT ON COLUMN P_HOP.HOP_MUNC IS 'Municipio nacimiento';
COMMENT ON COLUMN P_HOP.HOP_PAO IS 'País municipio origen';
COMMENT ON COLUMN P_HOP.HOP_PRO IS 'Provincia municipio origen';
COMMENT ON COLUMN P_HOP.HOP_MUO IS 'Municipio origen';
COMMENT ON COLUMN P_HOP.HOP_PAD IS 'País municipio destino';
COMMENT ON COLUMN P_HOP.HOP_PRD IS 'Provincia municipio destino';
COMMENT ON COLUMN P_HOP.HOP_MUD IS 'Municipio destino';
COMMENT ON COLUMN P_HOP.HOP_CONO IS 'Consulado origen';
COMMENT ON COLUMN P_HOP.HOP_OPEA IS 'Número de operación anterior';
COMMENT ON COLUMN P_HOP.HOP_FEC IS 'Fecha de grabación del histórico';
COMMENT ON COLUMN P_HOP.HOP_FOP IS 'Fecha de operación';
COMMENT ON COLUMN P_HOP.HOP_HAB IS 'Habitante al que se refiere la operación';
COMMENT ON COLUMN P_HOP.HOP_TID IS 'Tipo de documento';
COMMENT ON COLUMN P_HOP.HOP_DOC IS 'Documento';
COMMENT ON COLUMN P_HOP.HOP_NOM IS 'Nombre';
COMMENT ON COLUMN P_HOP.HOP_AP1 IS '1er Apellido';
COMMENT ON COLUMN P_HOP.HOP_PA1 IS 'Partícula 1er Apellido';
COMMENT ON COLUMN P_HOP.HOP_AP2 IS '2do Apellido';
COMMENT ON COLUMN P_HOP.HOP_PA2 IS 'Partícula 2do Apellido';
COMMENT ON COLUMN P_HOP.HOP_ORD IS 'Orden del habitante en la hoja padronal';
COMMENT ON COLUMN P_HOP.HOP_TEL IS 'Tel¿fono';
COMMENT ON COLUMN P_HOP.HOP_SEX IS 'Sexo';
COMMENT ON COLUMN P_HOP.HOP_FNA IS 'Fecha de nacimiento';
COMMENT ON COLUMN P_HOP.HOP_FEM IS 'Fecha de empadronamiento';
COMMENT ON COLUMN P_HOP.HOP_NIE IS 'Número de identificación electoral';
COMMENT ON COLUMN P_HOP.HOP_SIT IS 'Situación';
COMMENT ON COLUMN P_HOP.HOP_NIA IS 'Número de identificación del ayuntamiento';
COMMENT ON COLUMN P_HOP.HOP_NOF IS 'Nombre Fonético';
COMMENT ON COLUMN P_HOP.HOP_ACC IS 'Accón INE (1-No Enviar, 2-Enviar)';
COMMENT ON COLUMN P_HOP.HOP_FAC IS 'Fecha Acción INE';

CREATE INDEX REL_PH_31_FK ON P_HOP (HOP_COV, HOP_CAV);
CREATE INDEX REL_PH_32_FK ON P_HOP (HOP_PANC, HOP_PRNC, HOP_MUNC);
CREATE INDEX REL_PH_33_FK ON P_HOP (HOP_PAO, HOP_PRO, HOP_MUO);
CREATE INDEX REL_PH_34_FK ON P_HOP (HOP_PAD, HOP_PRD, HOP_MUD);
CREATE INDEX REL_PH_39_FK ON P_HOP (HOP_OPEA);
CREATE INDEX REL_PH_4_FK ON P_HOP (HOP_PAI, HOP_PRV, HOP_MUN, HOP_DIS, HOP_SEC, HOP_LET, HOP_HOJ, HOP_FAM, HOP_VERH);
CREATE INDEX REL_PH_40_FK ON P_HOP (HOP_PAN);
CREATE INDEX REL_PH_42_FK ON P_HOP (HOP_TIT);
CREATE INDEX REL_PH_50_FK ON P_HOP (HOP_TID);
CREATE INDEX REL_PH_25_FK ON P_HOP (HOP_OPE);
CREATE INDEX REL_PH_27_FK ON P_HOP (HOP_HAB);
CREATE INDEX REL_PH_28_FK ON P_HOP (HOP_CONO);
CREATE INDEX REL_PH_29_FK ON P_HOP (HOP_COD);
 

CREATE TABLE P_OPE (
	OPE_NUM NUMBER(9,0) NOT NULL, 
	OPE_PAI NUMBER(3,0) NOT NULL, 
	OPE_PRV NUMBER(2,0) NOT NULL, 
	OPE_MUN NUMBER(3,0) NOT NULL, 
	OPE_DIS NUMBER(2,0) NOT NULL, 
	OPE_SEC NUMBER(3,0) NOT NULL, 
	OPE_HOJ NUMBER(4,0) NOT NULL, 
	OPE_FAM NUMBER(2,0) NOT NULL, 
	OPE_VER NUMBER(3,0) NOT NULL, 
	OPE_TIT VARCHAR2(2), 
	OPE_PAN NUMBER(3,0), 
	OPE_PANC NUMBER(3,0), 
	OPE_PRNC NUMBER(2,0), 
	OPE_MUNC NUMBER(3,0), 
	OPE_PAO NUMBER(3,0), 
	OPE_PRO NUMBER(2,0), 
	OPE_MUO NUMBER(3,0), 
	OPE_PAD NUMBER(3,0), 
	OPE_PRD NUMBER(2,0), 
	OPE_MUD NUMBER(3,0), 
	OPE_CONO NUMBER(3,0), 
	OPE_COND NUMBER(3,0), 
	OPE_OPEA NUMBER(9,0), 
	OPE_FOP DATE NOT NULL, 
	OPE_FID NUMBER(5,0), 
	OPE_LIN NUMBER(5,0), 
	OPE_HAB NUMBER(9,0) NOT NULL, 
	OPE_DOC VARCHAR2(25), 
	OPE_NOM VARCHAR2(20) NOT NULL, 
	OPE_AP1 VARCHAR2(25) NOT NULL, 
	OPE_PA1 VARCHAR2(10), 
	OPE_AP2 VARCHAR2(25), 
	OPE_PA2 VARCHAR2(10), 
	OPE_ORD NUMBER(2,0) NOT NULL, 
	OPE_TEL VARCHAR2(40), 
	OPE_SEX VARCHAR2(1) NOT NULL, 
	OPE_FNA DATE NOT NULL, 
	OPE_FEM DATE, 
	OPE_NIE VARCHAR2(11), 
	OPE_SIT VARCHAR2(1) NOT NULL, 
	OPE_NIA VARCHAR2(15), 
	OPE_TID NUMBER(2,0) NOT NULL, 
	OPE_FSI DATE, 
	OPE_LET VARCHAR2(1) NOT NULL, 
	OPE_COV VARCHAR2(1) NOT NULL, 
	OPE_CAV VARCHAR2(2) NOT NULL, 
	OPE_NOF VARCHAR2(100), 
	OPE_ACC CHAR(1) DEFAULT 2 NOT NULL, 
	OPE_FAC DATE, 
	OPE_FDS NUMBER(3,0), 
	CONSTRAINT PK_P_OPE PRIMARY KEY (OPE_NUM), 
	CONSTRAINT FK_P_OPE_REL_PH_49_T_TID FOREIGN KEY (OPE_TID)
		REFERENCES T_TID (TID_COD), 
	CONSTRAINT FK_P_OPE_REL_PH_23_P_CON FOREIGN KEY (OPE_COND)
		REFERENCES P_CON (CON_COD), 
	CONSTRAINT FK_P_OPE_REL_PH_24_P_CON FOREIGN KEY (OPE_CONO)
		REFERENCES P_CON (CON_COD), 
	CONSTRAINT FK_P_OPE_REL_PH_20_P_HAB FOREIGN KEY (OPE_HAB)
		REFERENCES P_HAB (HAB_COD), 
	CONSTRAINT FK_P_OPE_REL_PH_3_P_HOJ FOREIGN KEY (OPE_HOJ, OPE_PAI, OPE_PRV, OPE_MUN, OPE_DIS, OPE_SEC, OPE_LET, OPE_FAM, OPE_VER)
		REFERENCES P_HOJ (HOJ_NUM, HOJ_PAI, HOJ_PRV, HOJ_MUN, HOJ_DIS, HOJ_SEC, HOJ_LET, HOJ_FAM, HOJ_VER), 
	CONSTRAINT FK_P_OPE_REL_PH_22_P_OPE FOREIGN KEY (OPE_OPEA)
		REFERENCES P_OPE (OPE_NUM), 
	CONSTRAINT FK_P_OPE_REL_PH_38_P_TIT FOREIGN KEY (OPE_TIT)
		REFERENCES P_TIT (TIT_COD), 
	CONSTRAINT FK_P_OPE_REL_PH_26_P_TOP FOREIGN KEY (OPE_COV, OPE_CAV)
		REFERENCES P_TOP (TOP_COV, TOP_CAV)
);

COMMENT ON TABLE P_OPE IS 'OPERACIONES DE HABITANTES';
COMMENT ON COLUMN P_OPE.OPE_PAO IS 'País de origen';
COMMENT ON COLUMN P_OPE.OPE_PRO IS 'Provincia de origen';
COMMENT ON COLUMN P_OPE.OPE_MUO IS 'Municipio de origen';
COMMENT ON COLUMN P_OPE.OPE_PAD IS 'País de destino';
COMMENT ON COLUMN P_OPE.OPE_PRD IS 'Provincia de destino';
COMMENT ON COLUMN P_OPE.OPE_MUD IS 'Municipio de destino';
COMMENT ON COLUMN P_OPE.OPE_CONO IS 'Consulado de origen';
COMMENT ON COLUMN P_OPE.OPE_COND IS 'Consulado de destino';
COMMENT ON COLUMN P_OPE.OPE_OPEA IS 'Número de operación anterior';
COMMENT ON COLUMN P_OPE.OPE_FOP IS 'Fecha de operación';
COMMENT ON COLUMN P_OPE.OPE_FID IS 'Código del fichero';
COMMENT ON COLUMN P_OPE.OPE_LIN IS 'Número de línea';
COMMENT ON COLUMN P_OPE.OPE_HAB IS 'Habitante al que se refiere esta operación';
COMMENT ON COLUMN P_OPE.OPE_DOC IS 'Documento';
COMMENT ON COLUMN P_OPE.OPE_NOM IS 'Nombre';
COMMENT ON COLUMN P_OPE.OPE_AP1 IS '1er Apellido';
COMMENT ON COLUMN P_OPE.OPE_PA1 IS 'Partícula 1er Apellido';
COMMENT ON COLUMN P_OPE.OPE_AP2 IS '2do Apellido';
COMMENT ON COLUMN P_OPE.OPE_PA2 IS 'Partícula 2do Apellido';
COMMENT ON COLUMN P_OPE.OPE_ORD IS 'Orden del habitante en la hoja padronal';
COMMENT ON COLUMN P_OPE.OPE_TEL IS 'Teléfono';
COMMENT ON COLUMN P_OPE.OPE_SEX IS 'Sexo';
COMMENT ON COLUMN P_OPE.OPE_FNA IS 'Fecha de nacimiento';
COMMENT ON COLUMN P_OPE.OPE_FEM IS 'Fecha de empadronamiento';
COMMENT ON COLUMN P_OPE.OPE_NIE IS 'Número de identificación electoral';
COMMENT ON COLUMN P_OPE.OPE_SIT IS 'Situación';
COMMENT ON COLUMN P_OPE.OPE_NIA IS 'Número de identificación del ayuntamiento';
COMMENT ON COLUMN P_OPE.OPE_TID IS 'Tipo de Documento';
COMMENT ON COLUMN P_OPE.OPE_LET IS 'Letra Sección';
COMMENT ON COLUMN P_OPE.OPE_COV IS 'Código Variación';
COMMENT ON COLUMN P_OPE.OPE_CAV IS 'Causa Variación';
COMMENT ON COLUMN P_OPE.OPE_NOF IS 'Nombre Fonético';
COMMENT ON COLUMN P_OPE.OPE_ACC IS 'Acción INE (1-No Enviar, 2-Enviar)';
COMMENT ON COLUMN P_OPE.OPE_FAC IS 'Fecha Acción INE';
COMMENT ON COLUMN P_OPE.OPE_NUM IS 'Número de operación';
COMMENT ON COLUMN P_OPE.OPE_PAI IS 'Código de país hoja padronal';
COMMENT ON COLUMN P_OPE.OPE_PRV IS 'Código de provincia hoja padronal';
COMMENT ON COLUMN P_OPE.OPE_MUN IS 'Código del municipio hoja padronal';
COMMENT ON COLUMN P_OPE.OPE_DIS IS 'Código de distrito hoja padronal';
COMMENT ON COLUMN P_OPE.OPE_SEC IS 'Código de sección hoja padronal';
COMMENT ON COLUMN P_OPE.OPE_HOJ IS 'Número de hoja padronal';
COMMENT ON COLUMN P_OPE.OPE_FAM IS 'Número de familia';
COMMENT ON COLUMN P_OPE.OPE_VER IS 'Número de modificación hoja padronal';
COMMENT ON COLUMN P_OPE.OPE_TIT IS 'Código de titulación';
COMMENT ON COLUMN P_OPE.OPE_PAN IS 'País de nacionalidad';
COMMENT ON COLUMN P_OPE.OPE_PANC IS 'País de nacimiento';
COMMENT ON COLUMN P_OPE.OPE_PRNC IS 'Provincia de nacimiento';
COMMENT ON COLUMN P_OPE.OPE_MUNC IS 'Municipio de nacimiento';

CREATE INDEX REL_PH_20_FK ON P_OPE (OPE_HAB);
CREATE INDEX REL_PH_26_FK ON P_OPE (OPE_COV, OPE_CAV);
CREATE INDEX REL_PH_3_FK ON P_OPE (OPE_PAI, OPE_PRV, OPE_MUN, OPE_DIS, OPE_SEC, OPE_LET, OPE_HOJ, OPE_FAM, OPE_VER);
CREATE INDEX REL_PH_35_FK ON P_OPE (OPE_PAO, OPE_PRO, OPE_MUO);
CREATE INDEX REL_PH_36_FK ON P_OPE (OPE_PANC, OPE_PRNC, OPE_MUNC);
CREATE INDEX REL_PH_37_FK ON P_OPE (OPE_PAD, OPE_PRD, OPE_MUD);
CREATE INDEX REL_PH_38_FK ON P_OPE (OPE_TIT);
CREATE INDEX REL_PH_41_FK ON P_OPE (OPE_PAN);
CREATE INDEX REL_PH_49_FK ON P_OPE (OPE_TID);
CREATE INDEX REL_PH_91_FK2 ON P_OPE (OPE_FID, OPE_LIN) ;
CREATE INDEX REL_PH_22_FK ON P_OPE (OPE_OPEA) ;
CREATE INDEX REL_PH_23_FK ON P_OPE (OPE_COND);
CREATE INDEX REL_PH_24_FK ON P_OPE (OPE_CONO);


CREATE TABLE P_FDS (
	FDS_COD NUMBER(3,0) NOT NULL, 
	FDS_DES VARCHAR2(25) NOT NULL, 
	FDS_TIP VARCHAR2(1) DEFAULT 'F' NOT NULL, 
	FDS_EST VARCHAR2(1) DEFAULT 'P' NOT NULL, 
	FDS_FEC DATE DEFAULT SYSDATE NOT NULL, 
	FDS_USU NUMBER(4,0) NOT NULL, 
	FDS_PAO NUMBER(3,0) NOT NULL, 
	FDS_PRO NUMBER(2,0) NOT NULL, 
	FDS_MUNO NUMBER(3,0) NOT NULL, 
	FDS_DOR NUMBER(2,0) NOT NULL, 
	FDS_SOR NUMBER(3,0) NOT NULL, 
	FDS_LOR VARCHAR2(1) DEFAULT NULL NOT NULL, 
	FDS_PAD NUMBER(3,0) NOT NULL, 
	FDS_PRD NUMBER(2,0) NOT NULL, 
	FDS_MUND NUMBER(3,0) NOT NULL, 
	FDS_DDE NUMBER(2,0) NOT NULL, 
	FDS_SDE NUMBER(3,0) NOT NULL, 
	FDS_LDE VARCHAR2(1) DEFAULT NULL NOT NULL, 
	CONSTRAINT PK_P_FDS PRIMARY KEY (FDS_COD), 
	CONSTRAINT FK_P_FDS_REL_TT_01_T_SEC FOREIGN KEY (FDS_PAO, FDS_PRO, FDS_MUNO, FDS_DOR, FDS_SOR, FDS_LOR)
		REFERENCES T_SEC (SEC_PAI, SEC_PRV, SEC_MUN, SEC_DIS, SEC_COD, SEC_LET), 
	CONSTRAINT FK_P_FDS_REL_TT_02_T_SEC FOREIGN KEY (FDS_PAD, FDS_PRD, FDS_MUND, FDS_DDE, FDS_SDE, FDS_LDE)
		REFERENCES T_SEC (SEC_PAI, SEC_PRV, SEC_MUN, SEC_DIS, SEC_COD, SEC_LET)
);


CREATE TABLE P_CAU (	
	CAU_COD NUMBER(2,0) NOT NULL, 
	CAU_DES VARCHAR2(60) NOT NULL, 
	CONSTRAINT PK_P_CAU PRIMARY KEY (CAU_COD)
);
 
COMMENT ON TABLE P_CAU IS 'CAUSAS CERTIFICADOS';
COMMENT ON COLUMN P_CAU.CAU_COD IS 'Código de causa';
COMMENT ON COLUMN P_CAU.CAU_DES IS 'Descripción';


CREATE TABLE P_CER (
	CER_EJE NUMBER(4,0) NOT NULL, 
	CER_ORD NUMBER(7,0) NOT NULL, 
	CER_CAR NUMBER(3,0), 
	CER_CAU NUMBER(2,0), 
	CER_TCR NUMBER(2,0) NOT NULL, 
	CER_CON NUMBER(5,0) NOT NULL, 
	CER_SIT VARCHAR2(1) NOT NULL, 
	CER_NCP NUMBER(1,0) NOT NULL, 
	CER_OBS VARCHAR2(255), 
	CER_FEM DATE NOT NULL, 
	CER_FCR DATE NOT NULL, 
	CER_FOP DATE NOT NULL, 
	CER_PER NUMBER(9,0), 
	CER_HAB NUMBER(9,0), 
	CONSTRAINT PK_P_CER PRIMARY KEY (CER_EJE, CER_ORD)
);

COMMENT ON TABLE P_CER IS 'CERTIFICADOS';
COMMENT ON COLUMN P_CER.CER_EJE IS 'Ejercicio';
COMMENT ON COLUMN P_CER.CER_ORD IS 'Número de orden';
COMMENT ON COLUMN P_CER.CER_CAR IS 'Código del cargo';
COMMENT ON COLUMN P_CER.CER_CAU IS 'Código de causa';
COMMENT ON COLUMN P_CER.CER_TCR IS 'Código de tipo de certificado';
COMMENT ON COLUMN P_CER.CER_CON IS 'Valor contador de tipo de certificado';
COMMENT ON COLUMN P_CER.CER_SIT IS 'Situación';
COMMENT ON COLUMN P_CER.CER_NCP IS 'Número de copias';
COMMENT ON COLUMN P_CER.CER_OBS IS 'Observaciones';
COMMENT ON COLUMN P_CER.CER_FEM IS 'Fecha de emisión';
COMMENT ON COLUMN P_CER.CER_FCR IS 'Fecha del certificado';
COMMENT ON COLUMN P_CER.CER_FOP IS 'Fecha de operación';

 
CREATE TABLE P_DFI (	
	DFI_FID NUMBER(5,0) NOT NULL, 
	DFI_LIN NUMBER(5,0) NOT NULL, 
	DFI_EST VARCHAR2(1) NOT NULL, 
	DFI_CON VARCHAR2(1000) NOT NULL, 
	DFI_OPE NUMBER(9,0), 
	DFI_OBS VARCHAR2(4000), 
	DFI_FEN DATE, 
	DFI_OBP VARCHAR2(4000), 
	CONSTRAINT PK_P_DFI PRIMARY KEY (DFI_FID, DFI_LIN)
);

COMMENT ON COLUMN P_DFI.DFI_OBP IS 'Observaciones personales';
 

CREATE TABLE P_EFM (	
	EFM_FID NUMBER(5,0) NOT NULL, 
	EFM_LIN NUMBER(5,0) NOT NULL, 
	EFM_ERI NUMBER(3,0) NOT NULL, 
	EFM_FCO DATE, 
	EFM_USU NUMBER(4,0), 
	EFM_OBS VARCHAR2(255), 
	CONSTRAINT PK_P_EFM PRIMARY KEY (EFM_FID, EFM_LIN, EFM_ERI)
);


CREATE TABLE P_EOP (	
	EOP_ENV NUMBER NOT NULL, 
	EOP_OPE NUMBER NOT NULL, 
	EOP_FEN DATE NOT NULL, 
	EOP_TINF VARCHAR2(1), 
	EOP_CDEV VARCHAR2(2), 
	CONSTRAINT PK_P_EOP PRIMARY KEY (EOP_ENV, EOP_OPE)
);

COMMENT ON TABLE P_EOP IS 'Histórico de envío de operaciones al INE (según creación de ficheros)';
COMMENT ON COLUMN P_EOP.EOP_ENV IS 'Número de envío';
COMMENT ON COLUMN P_EOP.EOP_OPE IS 'Código de operación';
COMMENT ON COLUMN P_EOP.EOP_FEN IS 'Fecha de envío';
COMMENT ON COLUMN P_EOP.EOP_TINF IS 'Tipo de información: R (Rechazada), C (Comunicación)';
COMMENT ON COLUMN P_EOP.EOP_CDEV IS 'Causa de devolución.';


CREATE TABLE P_ERI (	
	ERI_COD NUMBER(3,0) NOT NULL, 
	ERI_TIP VARCHAR2(1) NOT NULL, 
	ERI_MOT VARCHAR2(2), 
	ERI_CLA CHAR(10), 
	ERI_ACC VARCHAR2(4000), 
	ERI_TAC CHAR(10), 
	ERI_CVA VARCHAR2(1), 
	ERI_COV VARCHAR2(1), 
	ERI_CAV VARCHAR2(2), 
	ERI_CIN NUMBER(3,0) NOT NULL, 
	ERI_NOM VARCHAR2(500), 
	CONSTRAINT PK_P_ERI PRIMARY KEY (ERI_COD)
); 


CREATE TABLE P_FDD (	
	FDD_FDS NUMBER(4,0) NOT NULL, 
	FDD_COD NUMBER(5,0) NOT NULL, 
	FDD_PAI NUMBER(3,0) NOT NULL, 
	FDD_PRV NUMBER(2,0) NOT NULL, 
	FDD_MUN NUMBER(3,0) NOT NULL, 
	FDD_VIA NUMBER(12,0) NOT NULL, 
	FDD_TNU VARCHAR2(1) NOT NULL, 
	FDD_PNU NUMBER(4,0), 
	FDD_PLE VARCHAR2(1), 
	FDD_ULN NUMBER(4,0), 
	FDD_ULE VARCHAR2(1), 
	FDD_HAB NUMBER(5,0) DEFAULT 0 NOT NULL, 
	CONSTRAINT PK_P_FDD PRIMARY KEY (FDD_FDS, FDD_COD)
);


CREATE TABLE P_FID (	
	FID_COD NUMBER(5,0) NOT NULL, 
	FID_FID NUMBER(5,0), 
	FID_TFI NUMBER(2,0) NOT NULL, 
	FID_FIC VARCHAR2(64) NOT NULL, 
	FID_NRE NUMBER(3,0) NOT NULL, 
	FID_FRE DATE NOT NULL, 
	FID_FEC DATE NOT NULL, 
	FID_FDV DATE, 
	FID_FTO DATE, 
	FID_GEN NUMBER(1,0) NOT NULL, 
	FID_LIN NUMBER(7,0) NOT NULL, 
	FID_RLE NUMBER(7,0), 
	FID_RGB NUMBER(7,0), 
	FID_RER NUMBER(7,0), 
	FID_ALT NUMBER(7,0), 
	FID_BAJ NUMBER(7,0), 
	FID_MOD NUMBER(7,0), 
	FID_REC NUMBER(7,0), 
	FID_OBS VARCHAR2(255), 
	FID_EPR NUMBER(1,0), 
	FID_FED DATE NOT NULL, 
	FID_FEH DATE NOT NULL, 
	CONSTRAINT PK_P_FID PRIMARY KEY (FID_COD)
);
 

CREATE TABLE P_TCR (	
	TCR_COD NUMBER(2,0) NOT NULL, 
	TCR_DES VARCHAR2(60) NOT NULL, 
	TCR_PLT VARCHAR2(64) NOT NULL, 
	TCR_IND NUMBER(1,0) NOT NULL, 
	CONSTRAINT PK_P_TCR PRIMARY KEY (TCR_COD)
);

COMMENT ON TABLE P_TCR IS 'TIPOS DE CERTIFICADOS';
COMMENT ON COLUMN P_TCR.TCR_COD IS 'Código de tipo de certificado';
COMMENT ON COLUMN P_TCR.TCR_DES IS 'Descripción';
COMMENT ON COLUMN P_TCR.TCR_PLT IS 'Plantilla asociada';
COMMENT ON COLUMN P_TCR.TCR_IND IS 'Individual';

 
CREATE TABLE P_TFI (	
	TFI_COD NUMBER(2,0) NOT NULL, 
	TFI_DES VARCHAR2(80) NOT NULL, 
	TFI_FRN VARCHAR2(20) NOT NULL, 
	TFI_FIV DATE NOT NULL, 
	TFI_SEN VARCHAR2(2) NOT NULL, 
	TFI_OBS VARCHAR2(255), 
	CONSTRAINT PK_P_TFI PRIMARY KEY (TFI_COD)
);

COMMENT ON TABLE P_TFI IS 'TIPOS DE FICHEROS DE INTERCAMBIO';
COMMENT ON COLUMN P_TFI.TFI_COD IS 'Codigo de tipo';
COMMENT ON COLUMN P_TFI.TFI_DES IS 'Descripcion';
COMMENT ON COLUMN P_TFI.TFI_FRN IS 'Formato del nombre';
COMMENT ON COLUMN P_TFI.TFI_FIV IS 'Fecha de inicio de vigencia';
COMMENT ON COLUMN P_TFI.TFI_SEN IS 'Sentido (IP --> Desde el INE al Padrón / PI --> Desde el padrón al INE)';
COMMENT ON COLUMN P_TFI.TFI_OBS IS 'Observaciones';


CREATE OR REPLACE FORCE VIEW P_DFIV (DFI_FID, DFI_LIN, NOMBRE, DOCUMENTO, FECHA_NAC, NIA, COV, CAV) AS 
  SELECT dfi_fid, dfi_lin,
          (   TRIM (SUBSTR (dfi_con, 6, 20))
           || TRIM (SUBSTR (dfi_con, 26, 6))
           || TRIM (SUBSTR (dfi_con, 32, 25))
           || TRIM (SUBSTR (dfi_con, 57, 6))
           || TRIM (SUBSTR (dfi_con, 63, 25))
          ) AS nombre,
          TRIM (SUBSTR (dfi_con, 101, 31)) AS documento,
          SUBSTR (dfi_con, 93, 8) AS fecha_nac,
          TRIM (SUBSTR (dfi_con, 132, 15)) AS nia,
          SUBSTR (dfi_con, 169, 1) AS cov, SUBSTR (dfi_con, 170, 2) AS cav
     FROM p_dfi, p_fid
    WHERE (fid_tfi = 41 OR fid_tfi = 3) AND fid_cod = dfi_fid
;

CREATE OR REPLACE FORCE VIEW  V_PADR (HAB_COD, HAB_DOC, HAB_NOM, HAB_AP1, HAB_PA1, HAB_AP2, HAB_PA2, HAB_NOMC, HAB_ORD, HAB_TEL, HAB_SEX, HAB_FNA, HAB_FEM, HAB_NIE, HAB_SIT, HAB_NIA, HAB_NOF, HAB_FAR, TID_COD, TID_DES, TID_PFI, FNAC, FGRAB, HOJ_DOM, HOJ_LET, HOJ_VER, HOJ_DIS, HOJ_SEC, HOJ_NUM, OPE_COV, OPE_CAV, FOPE, DPO_ESC, DPO_PLT, DPO_TVV, DPO_PTA, DSU_MZI, DSU_NUD, DSU_LED, DSU_NUH, DSU_LEH, DSU_BLQ, DSU_POR, DSU_KMT, DSU_HMT, DSU_DOMC, VIA_TVI, VIA_NOM, CODPAIS, DESCPAIS, CODPROV, DESCPROV, CODMUN, DESCMUN, CODNAC, DESCNAC, CODPROVN, DESCPROVN, CODMUNN, DESCMUNN, CODCONSO, DESCCONSO, CODPAISO, DESCPAISO, CODPROVO, DESCPROVO, CODMUNO, DESCMUNO, CODCONSD, DESCCONSD, CODPAISD, DESCPAISD, CODPROVD, DESCPROVD, CODMUND, DESCMUND, NUC_COD, NUC_CIN, NUC_NOM, NUC_NOL, ESI_COD, ESI_ECO, ESI_CIN, ESI_DCO, ESI_NOM, ESI_NOL, ECO_COD, ECO_CIN, ECO_NOM, ECO_NOL, TIT_COD, TIT_DES) AS 
  SELECT DISTINCT p_hab.hab_cod, p_hab.hab_doc, p_hab.hab_nom, p_hab.hab_ap1,
                   p_hab.hab_pa1, p_hab.hab_ap2, p_hab.hab_pa2,
                      p_hab.hab_pa1
                   || ' '
                   || p_hab.hab_ap1
                   || ' '
                   || p_hab.hab_pa2
                   || ''
                   || p_hab.hab_ap2
                   || ', '
                   || p_hab.hab_nom AS titular,
                   p_hab.hab_ord, p_hab.hab_tel, p_hab.hab_sex,
                   TO_CHAR (hab_fna, 'DD/MM/YYYY') AS hab_fna,
                   TO_CHAR (hab_fem, 'DD/MM/YYYY') AS hab_fem, p_hab.hab_nie,
                   p_hab.hab_sit, p_hab.hab_nia, p_hab.hab_nof, p_hab.hab_far,
                   t_tid.tid_cod, t_tid.tid_des, t_tid.tid_pfi,
                   TO_CHAR (hab_fna, 'DD/MM/YYYY') AS fnac,
                   TO_CHAR (hab_fal, 'DD/MM/YYYY') AS fgrab, p_hoj.hoj_dom,
                   p_hoj.hoj_let, p_hoj.hoj_ver, p_hoj.hoj_dis, p_hoj.hoj_sec,
                   p_hoj.hoj_num, p_ope.ope_cov, p_ope.ope_cav,
                   TO_CHAR (ope_fop, 'DD/MM/YYYY') AS fope, t_dpo.dpo_esc,
                   t_dpo.dpo_plt, t_dpo.dpo_tvv, t_dpo.dpo_pta, t_dsu.dsu_mzi,
                   t_dsu.dsu_nud, t_dsu.dsu_led, t_dsu.dsu_nuh, t_dsu.dsu_leh,
                   t_dsu.dsu_blq, t_dsu.dsu_por, t_dsu.dsu_kmt, t_dsu.dsu_hmt,
                      t_tvi.tvi_des
                   || ' '
                   || t_via.via_nom
                   || ' '
                   || t_dsu.dsu_nud
                   || ' '
                   || t_dsu.dsu_led
                   || ' '
                   || t_dsu.dsu_nuh
                   || t_dsu.dsu_leh
                   || ' Bl. '
                   || t_dsu.dsu_blq
                   || ' Portal '
                   || t_dsu.dsu_por
                   || ' Esc. '
                   || t_dpo.dpo_esc
                   || t_dpo.dpo_plt
                   || ' ? '
                   || t_dpo.dpo_pta AS domicilio,
                   t_via.via_tvi, t_via.via_nom, pais.pai_cod AS codpais,
                   pais.pai_nom AS descpais, prv.prv_cod AS codprov,
                   prv.prv_nom AS descprov, mun.mun_cod AS codmun,
                   mun.mun_nom AS descmun, paisnac.pai_cod AS codnac,
                   paisnac.pai_nom AS descnac, prvn.prv_cod AS codprovn,
                   prvn.prv_nom AS descprovn, munn.mun_cod AS codmunn,
                   munn.mun_nom AS descmunn, conso.pai_cod AS codconso,
                   conso.pai_nom AS descconso, paiso.pai_cod AS codpaiso,
                   paiso.pai_nom AS descpaiso, prvo.prv_cod AS codprovo,
                   prvo.prv_nom AS descprovo, muno.mun_cod AS codmuno,
                   muno.mun_nom AS descmuno, consd.pai_cod AS codconsd,
                   consd.pai_nom AS descconsd, paisd.pai_cod AS codpaisd,
                   paisd.pai_nom AS descpaisd, prvd.prv_cod AS codprovd,
                   prvd.prv_nom AS descprovd, mund.mun_cod AS codmund,
                   mund.mun_nom AS descmund, t_nuc.nuc_cod, t_nuc.nuc_cin,
                   t_nuc.nuc_nom, t_nuc.nuc_nol, t_esi.esi_cod, t_esi.esi_eco,
                   t_esi.esi_cin, t_esi.esi_dco, t_esi.esi_nom, t_esi.esi_nol,
                   t_eco.eco_cod, t_eco.eco_cin, t_eco.eco_nom, t_eco.eco_nol,
                   p_tit.tit_cod, p_tit.tit_des
              FROM p_hab,
                   t_tid,
                   p_ope,
                   p_hoj,
                   t_trm,
                   t_via,
                   t_dpo,
                   t_dsu,
                   t_nuc,
                   t_esi,
                   t_eco,
                   SGE_GENERICO.t_pai pais,
                   SGE_GENERICO.t_prv prv,
                   SGE_GENERICO.t_mun mun,
                   SGE_GENERICO.t_pai paisnac,
                   SGE_GENERICO.t_prv prvn,
                   SGE_GENERICO.t_mun munn,
                   p_tit,
                   SGE_GENERICO.t_pai paiso,
                   SGE_GENERICO.t_prv prvo,
                   SGE_GENERICO.t_mun muno,
                   SGE_GENERICO.t_pai paisd,
                   SGE_GENERICO.t_prv prvd,
                   SGE_GENERICO.t_mun mund,
                   SGE_GENERICO.t_pai conso,
                   SGE_GENERICO.t_pai consd,
                   t_tvi
             WHERE
-- HAB_PAI=108 AND HAB_PRV=15 AND HAB_MUN=75 AND    -- Modificar con los valores adecuados de pais, provincia y municipio
                   hab_pai = hoj_pai
               AND hab_prv = hoj_prv
               AND hab_mun = hoj_mun
               AND hab_dis = hoj_dis
               AND hab_sec = hoj_sec
               AND hab_let = hoj_let
               AND hab_hoj = hoj_num
               AND hab_fam = hoj_fam
               AND hab_ver = hoj_ver
               AND hab_tit = tit_cod(+)
               AND hab_ope = ope_num(+)
               AND hab_cod = ope_hab(+)
               AND hab_tid = tid_cod(+)
               AND hab_pna = paisnac.pai_cod(+)
               AND hoj_sit = 'A'
               AND hoj_dom = dpo_dom
               AND dpo_dsu = dsu_cod
               AND dsu_pai = hab_pai
               AND dsu_prv = hab_prv
               AND dsu_mun = hab_mun
               AND dsu_pai = via_pai
               AND dsu_prv = via_prv
               AND dsu_mun = via_mun
               AND dsu_via = via_cod
               AND dsu_tpai = trm_pai(+)
               AND dsu_tprv = trm_prv(+)
               AND dsu_tmun = trm_mun(+)
               AND dsu_ttnu = trm_tnu(+)
               AND dsu_tvia = trm_via(+)
               AND dsu_tcod = trm_cod(+)
               AND trm_npa = nuc_pai(+)
               AND trm_npr = nuc_prv(+)
               AND trm_nmu = nuc_mun(+)
               AND trm_nes = nuc_esi(+)
               AND trm_nuc = nuc_cod(+)
               AND nuc_pai = esi_pai(+)
               AND nuc_prv = esi_prv(+)
               AND nuc_mun = esi_mun(+)
               AND nuc_esi = esi_cod(+)
               --AND (( ESI_ECO LIKE '1' ESCAPE '?'))
               AND esi_pai = eco_pai(+)
               AND esi_prv = eco_prv(+)
               AND esi_mun = eco_mun(+)
               AND esi_eco = eco_cod(+)
               AND hab_mun = mun.mun_cod
               AND hab_pai = mun.mun_pai
               AND hab_prv = mun.mun_prv
               AND mun.mun_prv = prv.prv_cod
               AND mun.mun_pai = prv.prv_pai
               AND prv.prv_pai = pais.pai_cod
               AND hab_mna = munn.mun_cod(+)
               AND hab_pan = munn.mun_pai(+)
               AND hab_prn = munn.mun_prv(+)
               AND munn.mun_prv = prvn.prv_cod(+)
               AND munn.mun_pai = prvn.prv_pai(+)
               AND ope_cono = conso.pai_cod(+)
               AND ope_muo = muno.mun_cod(+)
               AND ope_pao = muno.mun_pai(+)
               AND ope_pro = muno.mun_prv(+)
               AND muno.mun_prv = prvo.prv_cod(+)
               AND muno.mun_pai = prvo.prv_pai(+)
               AND prvo.prv_pai = paiso.pai_cod(+)
               AND ope_cond = consd.pai_cod(+)
               AND ope_mud = mund.mun_cod(+)
               AND ope_pad = mund.mun_pai(+)
               AND ope_prd = mund.mun_prv(+)
               AND mund.mun_prv = prvd.prv_cod(+)
               AND mund.mun_pai = prvd.prv_pai(+)
               AND prvd.prv_pai = paisd.pai_cod(+)
               AND t_via.via_tvi = t_tvi.tvi_cod(+);
 
  CREATE OR REPLACE FORCE VIEW  V_PAF (FNAC, FOPE, OPE_NUM, OPE_PAI, OPE_PRV, OPE_MUN, OPE_DIS, OPE_SEC, OPE_HOJ, OPE_FAM, OPE_VER, OPE_TIT, OPE_PAN, OPE_PANC, OPE_PRNC, OPE_MUNC, OPE_PAO, OPE_PRO, OPE_MUO, OPE_PAD, OPE_PRD, OPE_MUD, OPE_CONO, OPE_COND, OPE_OPEA, OPE_FOP, OPE_FID, OPE_LIN, OPE_HAB, OPE_DOC, OPE_NOM, OPE_AP1, OPE_PA1, OPE_AP2, OPE_PA2, OPE_ORD, OPE_TEL, OPE_SEX, OPE_FNA, OPE_FEM, OPE_NIE, OPE_SIT, OPE_NIA, OPE_TID, OPE_FSI, OPE_LET, OPE_COV, OPE_CAV, OPE_NOF, OPE_ACC, OPE_FAC, OPE_FDS, HOJ_PAI, HOJ_PRV, HOJ_MUN, HOJ_DIS, HOJ_SEC, HOJ_NUM, HOJ_FAM, HOJ_VER, HOJ_DOM, HOJ_URB, HOJ_CON, HOJ_SIT, HOJ_LET, DPO_DOM, DPO_DSU, DPO_ESC, DPO_PLT, DPO_TVV, DPO_PTA, DPO_OBS, DPO_SIT, DPO_FAL, DPO_UAL, DPO_FBJ, DPO_UBJ, DPO_FIV, DSU_COD, DSU_PAI, DSU_PRV, DSU_MUN, DSU_VIA, DSU_TNU, DSU_PAC, DSU_PRC, DSU_MUC, DSU_CPO, DSU_PAE, DSU_PRE, DSU_MUE, DSU_ESI, DSU_PAM, DSU_PRM, DSU_MUM, DSU_DIS, DSU_SEC, DSU_LET, DSU_MZI, DSU_HOJ, DSU_MNZ, DSU_PAR, DSU_NUD, DSU_LED, DSU_NUH, DSU_LEH, DSU_BLQ, DSU_POR, DSU_LUG, DSU_ORX, DSU_ORY, DSU_FIX, DSU_FIY, DSU_VER, DSU_KMT, DSU_HMT, DSU_IDO, DSU_SIT, DSU_FAL, DSU_UAL, DSU_FBJ, DSU_UBJ, DSU_FIV, DSU_TTNU, DSU_TPAI, DSU_TPRV, DSU_TMUN, DSU_TVIA, DSU_TCOD, VIA_PAI, VIA_PRV, VIA_MUN, VIA_COD, VIA_TVI, VIA_CBJ, VIA_CIN, VIA_NOM, VIA_NOC, VIA_NOA, VIA_CAT, VIA_FAP, VIA_EXP, VIA_LNG, VIA_AMX, VIA_AMI, VIA_TRF, VIA_STR, VIA_IDO, VIA_SIT, VIA_FAL, VIA_UAL, VIA_FBJ, VIA_UBJ, VIA_FIV, TRM_TNU, TRM_PAI, TRM_PRV, TRM_MUN, TRM_VIA, TRM_COD, TRM_SPA, TRM_SPR, TRM_SMU, TRM_SDI, TRM_MZI, TRM_SSPA, TRM_SSPR, TRM_SSMU, TRM_SSDI, TRM_SSSE, TRM_SSC, TRM_CPO, TRM_PNU, TRM_PLE, TRM_ULN, TRM_ULL, TRM_MPA, TRM_MPR, TRM_MMU, TRM_MDI, TRM_MSE, TRM_MLE, TRM_SSE, TRM_SLE, TRM_SSLE, TRM_NUC, TRM_NPA, TRM_NPR, TRM_NMU, TRM_NES, TRM_SIT, ECO_PAI, ECO_PRV, ECO_MUN, ECO_COD, ECO_CIN, ECO_NOM, ECO_NOL, ECO_IDO, ECO_SIT, ESI_PAI, ESI_PRV, ESI_MUN, ESI_COD, ESI_ECO, ESI_CIN, ESI_DCO, ESI_NOM, ESI_NOL, ESI_KMC, ESI_ALT, ESI_IDO, ESI_SIT, ESI_PAEC, ESI_PREC, ESI_MUEC, NUC_PAI, NUC_PRV, NUC_MUN, NUC_ESI, NUC_COD, NUC_CIN, NUC_NOM, NUC_NOL, NUC_IDO, NUC_SIT, NUC_FBA, NUC_UBA, NUC_FIV) AS 
  SELECT TO_CHAR (p_ope.ope_fna, 'DD/MM/YYYY') AS fnac,
          TO_CHAR (p_ope.ope_fop, 'DD/MM/YYYY') AS fope, p_ope.OPE_NUM,p_ope.OPE_PAI,p_ope.OPE_PRV,p_ope.OPE_MUN,p_ope.OPE_DIS,p_ope.OPE_SEC,p_ope.OPE_HOJ,p_ope.OPE_FAM,p_ope.OPE_VER,p_ope.OPE_TIT,p_ope.OPE_PAN,p_ope.OPE_PANC,p_ope.OPE_PRNC,p_ope.OPE_MUNC,p_ope.OPE_PAO,p_ope.OPE_PRO,p_ope.OPE_MUO,p_ope.OPE_PAD,p_ope.OPE_PRD,p_ope.OPE_MUD,p_ope.OPE_CONO,p_ope.OPE_COND,p_ope.OPE_OPEA,p_ope.OPE_FOP,p_ope.OPE_FID,p_ope.OPE_LIN,p_ope.OPE_HAB,p_ope.OPE_DOC,p_ope.OPE_NOM,p_ope.OPE_AP1,p_ope.OPE_PA1,p_ope.OPE_AP2,p_ope.OPE_PA2,p_ope.OPE_ORD,p_ope.OPE_TEL,p_ope.OPE_SEX,p_ope.OPE_FNA,p_ope.OPE_FEM,p_ope.OPE_NIE,p_ope.OPE_SIT,p_ope.OPE_NIA,p_ope.OPE_TID,p_ope.OPE_FSI,p_ope.OPE_LET,p_ope.OPE_COV,p_ope.OPE_CAV,p_ope.OPE_NOF,p_ope.OPE_ACC,p_ope.OPE_FAC,p_ope.OPE_FDS, p_hoj.HOJ_PAI,p_hoj.HOJ_PRV,p_hoj.HOJ_MUN,p_hoj.HOJ_DIS,p_hoj.HOJ_SEC,p_hoj.HOJ_NUM,p_hoj.HOJ_FAM,p_hoj.HOJ_VER,p_hoj.HOJ_DOM,p_hoj.HOJ_URB,p_hoj.HOJ_CON,p_hoj.HOJ_SIT,p_hoj.HOJ_LET,
          t_dpo.DPO_DOM,t_dpo.DPO_DSU,t_dpo.DPO_ESC,t_dpo.DPO_PLT,t_dpo.DPO_TVV,t_dpo.DPO_PTA,t_dpo.DPO_OBS,t_dpo.DPO_SIT,t_dpo.DPO_FAL,t_dpo.DPO_UAL,t_dpo.DPO_FBJ,t_dpo.DPO_UBJ,t_dpo.DPO_FIV, t_dsu.DSU_COD,t_dsu.DSU_PAI,t_dsu.DSU_PRV,t_dsu.DSU_MUN,t_dsu.DSU_VIA,t_dsu.DSU_TNU,t_dsu.DSU_PAC,t_dsu.DSU_PRC,t_dsu.DSU_MUC,t_dsu.DSU_CPO,t_dsu.DSU_PAE,t_dsu.DSU_PRE,t_dsu.DSU_MUE,t_dsu.DSU_ESI,t_dsu.DSU_PAM,t_dsu.DSU_PRM,t_dsu.DSU_MUM,t_dsu.DSU_DIS,t_dsu.DSU_SEC,t_dsu.DSU_LET,t_dsu.DSU_MZI,t_dsu.DSU_HOJ,t_dsu.DSU_MNZ,t_dsu.DSU_PAR,t_dsu.DSU_NUD,t_dsu.DSU_LED,t_dsu.DSU_NUH,t_dsu.DSU_LEH,t_dsu.DSU_BLQ,t_dsu.DSU_POR,t_dsu.DSU_LUG,t_dsu.DSU_ORX,t_dsu.DSU_ORY,t_dsu.DSU_FIX,t_dsu.DSU_FIY,t_dsu.DSU_VER,t_dsu.DSU_KMT,t_dsu.DSU_HMT,t_dsu.DSU_IDO,t_dsu.DSU_SIT,t_dsu.DSU_FAL,t_dsu.DSU_UAL,t_dsu.DSU_FBJ,t_dsu.DSU_UBJ,t_dsu.DSU_FIV,t_dsu.DSU_TTNU,t_dsu.DSU_TPAI,t_dsu.DSU_TPRV,t_dsu.DSU_TMUN,t_dsu.DSU_TVIA,t_dsu.DSU_TCOD, t_via.VIA_PAI,t_via.VIA_PRV,t_via.VIA_MUN,t_via.VIA_COD,t_via.VIA_TVI,t_via.VIA_CBJ,t_via.VIA_CIN,t_via.VIA_NOM,t_via.VIA_NOC,t_via.VIA_NOA,t_via.VIA_CAT,t_via.VIA_FAP,t_via.VIA_EXP,t_via.VIA_LNG,t_via.VIA_AMX,t_via.VIA_AMI,t_via.VIA_TRF,t_via.VIA_STR,t_via.VIA_IDO,t_via.VIA_SIT,t_via.VIA_FAL,t_via.VIA_UAL,t_via.VIA_FBJ,t_via.VIA_UBJ,t_via.VIA_FIV, t_trm.TRM_TNU,t_trm.TRM_PAI,t_trm.TRM_PRV,t_trm.TRM_MUN,t_trm.TRM_VIA,t_trm.TRM_COD,t_trm.TRM_SPA,t_trm.TRM_SPR,t_trm.TRM_SMU,t_trm.TRM_SDI,t_trm.TRM_MZI,t_trm.TRM_SSPA,t_trm.TRM_SSPR,t_trm.TRM_SSMU,t_trm.TRM_SSDI,t_trm.TRM_SSSE,t_trm.TRM_SSC,t_trm.TRM_CPO,t_trm.TRM_PNU,t_trm.TRM_PLE,t_trm.TRM_ULN,t_trm.TRM_ULL,t_trm.TRM_MPA,t_trm.TRM_MPR,t_trm.TRM_MMU,t_trm.TRM_MDI,t_trm.TRM_MSE,t_trm.TRM_MLE,t_trm.TRM_SSE,t_trm.TRM_SLE,t_trm.TRM_SSLE,t_trm.TRM_NUC,t_trm.TRM_NPA,t_trm.TRM_NPR,t_trm.TRM_NMU,t_trm.TRM_NES,t_trm.TRM_SIT, t_eco.ECO_PAI,t_eco.ECO_PRV,t_eco.ECO_MUN,t_eco.ECO_COD,t_eco.ECO_CIN,t_eco.ECO_NOM,t_eco.ECO_NOL,t_eco.ECO_IDO,t_eco.ECO_SIT, t_esi.ESI_PAI,t_esi.ESI_PRV,t_esi.ESI_MUN,t_esi.ESI_COD,t_esi.ESI_ECO,t_esi.ESI_CIN,t_esi.ESI_DCO,t_esi.ESI_NOM,t_esi.ESI_NOL,t_esi.ESI_KMC,t_esi.ESI_ALT,t_esi.ESI_IDO,t_esi.ESI_SIT,t_esi.ESI_PAEC,t_esi.ESI_PREC,t_esi.ESI_MUEC, t_nuc.NUC_PAI,t_nuc.NUC_PRV,t_nuc.NUC_MUN,t_nuc.NUC_ESI,t_nuc.NUC_COD,t_nuc.NUC_CIN,t_nuc.NUC_NOM,t_nuc.NUC_NOL,t_nuc.NUC_IDO,t_nuc.NUC_SIT,t_nuc.NUC_FBA,t_nuc.NUC_UBA,t_nuc.NUC_FIV
     FROM p_ope, p_hoj, t_dpo, t_dsu, t_via, t_trm, t_nuc, t_esi, t_eco
    WHERE ope_num IN (SELECT   MAX (ope_num)
                          FROM p_ope
                         WHERE ope_fop <= TO_DATE ('01/01/2005', 'DD/MM/YYYY')
                      GROUP BY ope_hab)
      AND p_ope.ope_pai = hoj_pai
      AND p_ope.ope_prv = hoj_prv
      AND p_ope.ope_mun = hoj_mun
      AND p_ope.ope_dis = hoj_dis
      AND p_ope.ope_sec = hoj_sec
      AND p_ope.ope_let = hoj_let
      AND p_ope.ope_hoj = hoj_num
      AND p_ope.ope_fam = hoj_fam
      AND p_ope.ope_ver = hoj_ver
      AND hoj_dom = dpo_dom
      AND dpo_dsu = dsu_cod
      AND dsu_pai = via_pai
      AND dsu_prv = via_prv
      AND dsu_mun = via_mun
      AND dsu_via = via_cod
      AND dsu_tpai = trm_pai(+)
      AND dsu_tprv = trm_prv(+)
      AND dsu_tmun = trm_mun(+)
      AND dsu_ttnu = trm_tnu(+)
      AND dsu_tvia = trm_via(+)
      AND dsu_tcod = trm_cod(+)
      AND trm_npa = nuc_pai(+)
      AND trm_npr = nuc_prv(+)
      AND trm_nmu = nuc_mun(+)
      AND trm_nes = nuc_esi(+)
      AND trm_nuc = nuc_cod(+)
      AND nuc_pai = esi_pai(+)
      AND nuc_prv = esi_prv(+)
      AND nuc_mun = esi_mun(+)
      AND nuc_esi = esi_cod(+)
      AND esi_pai = eco_pai(+)
      AND esi_prv = eco_prv(+)
      AND esi_mun = eco_mun(+)
      AND esi_eco = eco_cod(+)
;