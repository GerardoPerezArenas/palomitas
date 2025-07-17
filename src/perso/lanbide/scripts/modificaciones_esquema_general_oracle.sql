-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.03.00 --------------------------
-----------------------------------------------------------------------
-- Tarea #485288: REGISTRO- [17084] - Traduccion Literales - Generales y Adaptaciones Retramitacion documentos por cambio Procedimiento
-- 06/04/2023
-- David Guerrero
-- Se anhaden etiquetas para multilenguaje
-- INI
UPDATE A_TEX  set TEX_DES ='Bidali idazpena' where TEX_APL=1 and TEX_COD='gbEnviarAsiento' and TEX_IDI=4; 
UPDATE A_TEX  set TEX_DES ='Nahitaezko eremua' where TEX_APL=1 and TEX_COD='msgRetramiDocCambioCampoOblig' and TEX_IDI=4; 
UPDATE A_TEX  set TEX_DES ='Hautatu Atzera egiteko prozedura baimendua (Owner)' where TEX_APL=1 and TEX_COD='msgRetramiDocCambioProSelOwner' and TEX_IDI=4; 
UPDATE A_TEX  set TEX_DES ='Dokumentuen erregistroa/erretramitazioa prozedura aldatzeagatik' where TEX_APL=1 and TEX_COD='msgRetramiDocCambioProTitulo' and TEX_IDI=4; 
UPDATE A_TEX  set TEX_DES ='Digitalizazio-prozesuan huts egin duten dokumentuak erregistratzeko, sarrera digitalizazio-amaierako egoeran eduki behar duzu (R_RES taula: FIN_DIGITALIZACION = 0) eta dokumentuak konpultsatuak izan behar dira (R_RED taula: RED_DOCDIGIT = 1).' where TEX_APL=1 and TEX_COD='msgRetramiDocCambioRegisInfo' and TEX_IDI=4; 
UPDATE A_TEX  set TEX_DES ='Adierazi erregistratu beharreko OIDa' where TEX_APL=1 and TEX_COD='msgRetramiDocCambioRegisOID' and TEX_IDI=4; 
UPDATE A_TEX  set TEX_DES ='Erregistratu' where TEX_APL=1 and TEX_COD='msgRetramiDocCambioRegistrar' and TEX_IDI=4; 
UPDATE A_TEX  set TEX_DES ='Erregistro aldatuaren idatzoharra. Baina ezin izan zen dokumentazioa atzera bota. Baliteke dokumentuak behar bezala ez ikustea; kontsultatu euskarri-ekipoa.' where TEX_APL=1 and TEX_COD='msjErrorModifRegRetramitar' and TEX_IDI=4; 

COMMIT;

Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocRegistrarTodos','4','Erregistratu Dokusin Sarrerako dokumentu guztiak',null,null);
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocRegistrarDocEspec','4','Dokusin sarrera-dokumentu espezifiko bat erregistratzea',null,null);
update a_tex  set tex_des='Dok. erretramitatu'  where tex_cod='msgRetramiDocCambioRetramitar' and tex_idi=4;

-- Retramitado Correctamente.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocOK','4','Behar bezala erretramitatua.',null,null);
-- No Retramitado.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNOOK','4','Erretramitatu gabea.',null,null);
-- OID no recuperado.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNoOID','4','Berreskuratu gabeko OID.',null,null);
-- No se han recuperado los documentos  asociados a la entrada.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNoDocs','4','Sarrerarekin lotutako dokumentuak ez dira berreskuratu.',null,null);
-- Error al procesar la operacion.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocERROROpe','4','Errorea eragiketa prozesatzean.',null,null);
-- Registrado  Correctamente.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocRegOK','4','Behar bezala erregistratuta.',null,null);
-- No Registrado.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocRegNOOK','4','Erregistratu gabe.',null,null);
-- No es un documento Compulsado.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNoComp','4','Ez da dokumentu konpultsatu bat.',null,null);
-- Datos del Documento no recuperados de BD para ese OID.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNODataBD','4','Datu-basetik berreskuratu gabeko dokumentuaren datuak OID horretarako.',null,null);

commit;
-- FIN