

-- FLEXIA 11.11.03. SCRIPTS PERSONALIZADOS PARA LANBIDE QUE INSERTAN CAMPOS SUPLEMENTARIOS DE TERCERO

-- Fecha de nacimiento
INSERT INTO T_CAMPOS_EXTRA(COD_MUNICIPIO,COD_CAMPO,ROTULO,DESCRIPCION,COD_PLANTILLA,TIPO_DATO,
TAMANO,MASCARA,OBLIGATORIO,ORDEN,ACTIVO,DESPLEGABLE)
VALUES(0,'TFECNACIMIENTO','FECHA DE NACIMIENTO','FECHA DE NACIMIENTO',4,3,
10,NULL,0,1,'SI',NULL);

INSERT INTO T_CAMPOS_EXTRA(COD_MUNICIPIO,COD_CAMPO,ROTULO,DESCRIPCION,COD_PLANTILLA,TIPO_DATO,
TAMANO,MASCARA,OBLIGATORIO,ORDEN,ACTIVO,DESPLEGABLE)
VALUES(0,'TSEXOTERCERO','SEXO','SEXO',6,6,
0,NULL,0,2,'SI','SEXO');

INSERT INTO T_CAMPOS_EXTRA(COD_MUNICIPIO,COD_CAMPO,ROTULO,DESCRIPCION,COD_PLANTILLA,TIPO_DATO,
TAMANO,MASCARA,OBLIGATORIO,ORDEN,ACTIVO,DESPLEGABLE)
VALUES(0,'TNACIONTERCERO','NACIONALIDAD','NACIONALIDAD',6,6,
0,NULL,0,3,'SI','NACI');

COMMIT;

-- Scripts desplegable sexo
INSERT INTO E_DES(DES_COD,DES_NOM) VALUES('SEXO','SEXO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('SEXO','1','HOMBRE / GIZONEZKOA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('SEXO','2','MUJER / EMAKUMEZKOA');

INSERT INTO E_DES(DES_COD,DES_NOM) VALUES('NACI','NACIONALIDAD');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','004','AFGANIST�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','008','ALBANIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','010','ANT�RTIDA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','012','ARGELIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','016','SAMOA AMERICANA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','020','ANDORRA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','024','ANGOLA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','028','ANTIGUA Y BARBUDA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','031','AZERBAY�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','032','ARGENTINA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','036','AUSTRALIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','040','AUSTRIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','044','BAHAMAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','048','BAHREIN');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','050','BANGLADESH');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','051','ARMENIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','052','BARBADOS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','056','B�LGICA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','060','BERMUDAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','064','BUT�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','068','BOLIVIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','070','BOSNIA-HERZEGOVINA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','072','BOTSWANA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','074','BOUVET, ISLA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','076','BRASIL');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','084','BELICE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','086','OC�ANO INDICO, TERRITORIO BRIT�NICO DEL');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','090','SALOMON, ISLAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','092','V�RGENES, ISLAS (BRIT�NICAS)');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','096','BRUNEI DARUSSALAM');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','100','BULGARIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','104','MYANMAR');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','108','BURUNDI');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','112','BIELORUSIA (BELARUS)');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','116','CAMBOYA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','120','CAMER�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','124','CANAD�');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','132','CABO VERDE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','136','CAIM�N, ISLAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','140','CENTROAFRICANA, REP�BLICA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','144','SRI LANKA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','148','CHAD');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','152','CHILE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','156','CHINA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','158','TAIWAN, PROVINCIA DE CHINA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','162','CHRISTMAS, ISLA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','166','COCOS (KEELING), ISLAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','170','COLOMBIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','174','COMORES');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','175','MAYOTTE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','178','CONGO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','180','CONGO REPUBLICA DEMOCR�TICA (EX ZAIRE)');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','184','COOK, ISLAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','188','COSTA RICA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','191','CROACIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','192','CUBA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','196','CHIPRE');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','203','CHECA, REP�BLICA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','204','BENIN');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','208','DINAMARCA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','212','DOMINICA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','214','DOMINICANA, REP�BLICA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','218','ECUADOR');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','222','EL SALVADOR');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','226','GUINEA ECUATORIAL');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','231','ETIOP�A');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','232','ERITREA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','233','ESTONIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','234','FEROE, ISLAS');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','238','MALVINAS, ISLAS (FALKLAND)');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','239','GEORGIA DEL SUR E ISLAS SANDWICH');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','242','FIDJI');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','246','FINLANDIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','249','FRANCIA, METROPOLITANA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','250','FRANCIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','254','GUAYANA FRANCESA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','258','POLINESIA FRANCESA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','260','TIERRAS AUSTRALES FRANCESAS');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','262','DJIBUTI');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','266','GAB�N');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','268','GEORGIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','270','GAMBIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','276','ALEMANIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','288','GHANA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','292','GIBRALTAR');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','296','KIRIBATI');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','300','GRECIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','304','GROENLANDIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','308','GRANADA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','312','GUADALUPE');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','316','GUAM');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','320','GUATEMALA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','324','GUINEA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','328','GUYANA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','332','HAIT�');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','334','HEARD Y MC DONALD, ISLAS');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','336','VATICANO, ESTADO DE LA CIUDAD DEL');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','340','HONDURAS');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','344','HONG KONG');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','348','HUNGR�A');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','352','ISLANDIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','356','INDIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','360','INDONESIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','364','IR�N, REP�BLICA ISL�MICA DE');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','368','IRAQ');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','372','IRLANDA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','376','ISRAEL');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','380','ITALIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','384','COSTA DE MARFIL');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','388','JAMAICA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','392','JAP�N');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','398','KAZAKSTAN');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','400','JORDANIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','404','KENIA');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','408','COREA, REP�BLICA POPULAR DEMOCR�TICA DE');	
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','410','COREA, REP�BLICA DE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','414','KUWAIT');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','417','KIRGHIZISTAN');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','418','LAOS, REP�BLICA DEMOCR�TICA POPULAR');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','422','L�BANO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','426','LESOTHO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','428','LETONIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','430','LIBERIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','434','LIBIA, JAMAHIRIYA �RABE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','438','LIECHTENSTEIN');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','440','LITUANIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','442','LUXEMBURGO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','446','MACAO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','450','MADAGASCAR');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','454','MALAWI');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','458','MALASIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','462','MALDIVAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','466','MAL�');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','470','MALTA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','474','MARTINICA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','478','MAURITANIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','480','MAURICIO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','484','M�JICO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','492','M�NACO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','496','MONGOLIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','498','MOLDAVIA, REP�BLICA DE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','499','MONTENEGRO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','500','MONTSERRAT');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','504','MARRUECOS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','508','MOZAMBIQUE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','512','OM�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','516','NAMIBIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','520','NAURU');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','524','NEPAL');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','528','HOLANDA (PAISES BAJOS)');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','530','ANTILLAS HOLANDESAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','533','ARUBA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','540','NUEVA CALEDONIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','548','VANUATU');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','554','NUEVA ZELANDA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','558','NICARAGUA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','562','N�GER');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','566','NIGERIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','570','NIUE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','574','NORFOLK, ISLA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','578','NORUEGA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','580','MARIANAS DEL NORTE, ISLAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','581','EEUU, ISLAS EXTERIORES MENORES');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','583','MICRONESIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','584','MARSHALL, ISLAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','585','PALAU');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','586','PAKIST�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','591','PANAM�');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','598','PAPUA, NUEVA GUINEA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','600','PARAGUAY');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','604','PER�');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','608','FILIPINAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','609','PALESTINA, TERRIORIO OCUPADO DE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','612','PITCAIRN');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','616','POLONIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','620','PORTUGAL');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','624','GUINEA BISSAU');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','626','TIMOR ORIENTAL');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','630','PUERTO RICO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','634','QATAR');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','638','REUNI�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','642','RUMANIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','643','RUSIA, FEDERACION DE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','646','RUANDA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','654','SANTA HELENA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','659','SAN CRIST�BAL Y NIEVES');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','660','ANGUILA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','662','SANTA LUC�A');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','666','SAN PEDRO Y MIQUELON');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','670','SAN VICENTE Y GRANADINAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','674','SAN MARINO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','678','SANTO TOMAS Y PRINCIPE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','682','ARABIA SAUD�');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','686','SENEGAL');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','688','SERBIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','690','SEYCHELLES');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','694','SIERRA LEONA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','702','SINGAPUR');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','703','ESLOVAQUIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','704','VIETNAM');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','705','ESLOVENIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','706','SOMALIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','710','AFRICA DEL SUR');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','716','ZIMBABWE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','724','ESPA�A');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','732','S�HARA OCCIDENTAL');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','736','SUD�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','740','SURINAM');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','744','SVALBARD Y JAN MAYEN, ISLAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','752','SUECIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','756','SUIZA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','760','SIRIA, REP�BLICA �RABE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','762','TAYIKIST�N (TADJIKIST�N, TAJIKIST�N)');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','764','TAILANDIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','768','TOGO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','772','TOKELAU');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','776','TONGA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','780','TRINIDAD Y TOBAGO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','784','EMIRATOS �RABES UNIDOS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','788','T�NEZ');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','792','TURQU�A');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','795','TURKMENIST�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','796','TURKS Y CAICOS, ISLAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','798','TUVALU');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','800','UGANDA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','804','UCRANIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','807','MACEDONIA, EX-REP�BLICA YUGOESLAVA DE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','818','EGIPTO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','826','REINO UNIDO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','834','TANZANIA, REP�BLICA UNIDA DE');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','840','ESTADOS UNIDOS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','850','V�RGENES, ISLAS (EEUU)');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','854','BURKINA FASO');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','858','URUGUAY');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','860','UZBEKIST�N');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','862','VENEZUELA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','876','WALLIS Y FUTUNA, ISLAS');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','882','SAMOA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','887','YEMEN');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','894','ZAMBIA');
INSERT INTO E_DES_VAL(DES_COD,DES_VAL_COD,DES_NOM) VALUES('NACI','999','AP�TRIDA');

