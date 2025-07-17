-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.02 --------------------------
-----------------------------------------------------------------------

-- Inserts correccion para version 17
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (11,'msjErrorFirmarDocumento',1,'Se ha detectado un error al intentar firmar el documento',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (11,'msjDocLocalFirmadoOk',1,'El documento ha sido firmado correctamente en su equipo. Pulse Aceptar para guardar la firma definitivamente en el servidor.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (11,'msjDocLocalNoFirmado',1,'El documento no se ha firmado.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (11,'ErrCargarAppletAutoFirma',1,'Ha ocurrido un error al cargar el componente de Autofirma.',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (11,'ErrCargarApplet',1,'Ha ocurrido un error al cargar el componente de firma.',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (11,'ErrDescargarFicheroAFirmar',1,'Ha ocurrido un error al intentar descargar el documento a firmar.',NULL,NULL);
GO

-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.02 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.03 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.03 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.04 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.04 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.05 --------------------------
-----------------------------------------------------------------------

-- Tareas #321418 [Flexia] Añadir el soporte a los nuevos flujos de firmas en la importación/exportación del XPDL
-- Alejandro Díaz
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (20,'errNoExisteUsuarioFirmaFlujo',1,'No existe el usuario determinado en la importación ',null,null);

GO

-- Tareas #324072 [Flexia] Corrección de errores en la importación / exportación XPDL
-- Alejandro Díaz
-- Actualización de mensajes de error para amoldarse a la nueva lógica.
UPDATE A_TEX SET TEX_DES='Error en tipo de procedimiento, que Debe figurar como "ExtendedAttribute" de nombre "tipo_procedimiento", y con valor numérico válido' WHERE TEX_COD='errProcTipoProc' AND TEX_IDI=1;
UPDATE A_TEX SET TEX_DES='Erro en tipo de procedemento, que Debe figurar como "ExtendedAttribute" de nome "tipo_procedimiento", e con valor numérico válido' WHERE TEX_COD='errProcTipoProc' AND TEX_IDI=2;

UPDATE A_TEX SET TEX_DES='Error en indicador de si un documento de un trámite está activo (no ha sido borrado). Debe figurar como "ExtendedAttribute" del documento de trámite, con nombre "docActivo" y valor "S", "N", "L", "O" o "U"' WHERE TEX_COD='errTramDocAct' AND TEX_IDI=1;
UPDATE A_TEX SET TEX_DES='Erro en indicador de se un documento dun trámite está activo (non foi borrado). Debe figurar como "ExtendedAttribute" do documento de trámite, con nome "docActivo" e valor "S", "N", "L", "O" ou "U"' WHERE TEX_COD='errTramDocAct' AND TEX_IDI=2;

GO

-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.05 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.06 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.06 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.07 --------------------------
-----------------------------------------------------------------------

-- #336204: [Sanse] Al enviar la notificación se seleccionan varios interesados pero se envía a uno solo.
-- Manuel José Méijome
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqInteresadoNotif',1,'Interesado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqInteresadoNotif',2,'Interesado',NULL,NULL);

GO

-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.07 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.10 --------------------------
-----------------------------------------------------------------------

-- Tarea #351395: [Sanse] Funcionamiento incorrecto al eliminar/recuperar un documento desde el m�dulo de documentos
-- Milagros Noya (25/01/2019)
update a_tex set tex_des='Documento recuperado' where tex_apl=6 and tex_idi=1 And Tex_Cod='docRecuperado';
Insert Into A_Tex (Tex_Apl,Tex_Cod,Tex_Idi,Tex_Des,Tex_Uso,Tex_Fec) Values (6,'docDesactivado',1,'Documento desactivado',Null,Null);
Insert Into A_Tex (Tex_Apl,Tex_Cod,Tex_Idi,Tex_Des,Tex_Uso,Tex_Fec) Values (6,'gbDesactivar',1,'Desactivar',Null,Null);
Insert Into A_Tex (Tex_Apl,Tex_Cod,Tex_Idi,Tex_Des,Tex_Uso,Tex_Fec) Values (6,'gbActivar',1,'Activar',Null,Null);
Insert Into A_Tex (Tex_Apl,Tex_Cod,Tex_Idi,Tex_Des,Tex_Uso,Tex_Fec) Values (4,'docDesactivado',1,'Documento desactivado',Null,Null);

GO
