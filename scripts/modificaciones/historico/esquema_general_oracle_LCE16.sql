-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.00.01 --------------------------
-----------------------------------------------------------------------


-- Tarea #247202 Gesti?n de unidades org?nicas dadas de baja

-- Adri?n Freixeiro (05/09/2016)

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'ocultar',1,'Ocultar',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'ocultar',2,'Agachar',NULL,NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'ocultar',1,'Ocultar',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'ocultar',2,'Agachar',NULL,NULL);

commit;

--Tarea #253692: Nuevo campo de expediente para ubicación de documentación. Parametrizable
--Milagros Noya (16/11/2016)
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjUbicDocFormatoNoValido',1,'El valor del campo "Ubicación documentación" tiene un formato no válido.',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjUbicDocFormatoNoValido',2,'O valor do campo "Ubicación documentación" ten un formato non válido.',NULL,NULL);




Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gbAdjuntarExpInt',1,'Adjuntar Expte. Interesado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gbAdjuntarExpInt',2,'Adxuntar Expte. Interesado',NULL,NULL);



Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqDesdeRegTelematico',1,'De Registro Telemático',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqDesdeRegTelematico',2,'De rexistro telemático',NULL,Null);


INSERT INTO A_TEX VALUES (4,'titExpMismoInteresado',1,'Expedientes con mismo/s interesado/s', null, null);




-- Tarea #245848: [CONS] Listado de usuarios
-- Milagros Noya (22/09/2016)
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'etiqBuscarUsusPorUOR',1,'Buscar por Unidad Orgánica',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'etiqBuscarUsusPorUOR',2,'Buscar por Unidade Orgánica',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'etiqBuscarUsusPorUOR',1,'Buscar por Unidad Orgánica',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'etiqBuscarUsusPorUOR',2,'Buscar por Unidade Orgánica',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'etiqBuscarUsusPorProc',1,'Buscar por Procedimiento',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'etiqBuscarUsusPorProc',2,'Buscar por Procedemento',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'etiqBuscarUsusPorProc',1,'Buscar por Procedimiento',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'etiqBuscarUsusPorProc',2,'Buscar por Procedemento',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'etiqListaUsusTitInf',1,'Listado de Usuarios',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'etiqListaUsusTitInf',2,'Listaxe de Usuarios',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'etiqListaUsusTitInf',1,'Listado de Usuarios',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'etiqListaUsusTitInf',2,'Listaxe de Usuarios',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'gbImprimir',1,'Imprimir',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'gbImprimir',2,'Imprimir',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'gbImprimir',1,'Imprimir',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'gbImprimir',2,'Imprimir',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'msgErrGenServ',1,'Ha ocurrido un error genérico en la comunicación cliente-servidor.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'msgErrGenServ',2,'Produciuse un erro xenérico na comunicación cliente-servidor.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'msgErrGenServ',1,'Ha ocurrido un error genérico en la comunicación cliente-servidor.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'msgErrGenServ',2,'Produciuse un erro xenérico na comunicación cliente-servidor.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'msgErrConexionBD',1,'Error al obtener una conexión a la base de datos',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'msgErrConexionBD',2,'Produciuse un erro o obter unha conexión a base de datos',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'msjErrPropBusq',1,'Ha ocurrido un error al recuperar los datos.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'msjErrPropBusq',2,'Produciuse un erro ó recuperar os datos.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'msgErrGenerarInf',1,'Ha ocurrido un error al generar el informe.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'msgErrGenerarInf',2,'Produciuse un erro ó xerar o informe.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'msgErrGenerarInf',1,'Ha ocurrido un error al generar el informe.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'msgErrGenerarInf',2,'Produciuse un erro ó xerar o informe.',NULL,NULL);
COMMIT;



-- Tarea #234108: Registro - Obligatoriedad documento interesado según asunto

-- Milagros Noya (11/05/2016)

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiq_tipoDocObligatorio',1,'No permite un interesado con tipo de documento "Sin documento"',NULL,NULL);		

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiq_tipoDocObligatorio',2,'Non permite un interesado con tipo de documento "Sin documento"',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'msj_NoCumpleTipoDocOblig',1,'El asunto seleccionado no permite un interesado "Sin documento"',NULL,NULL);		

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'msj_NoCumpleTipoDocOblig',2,'O asunto seleccionado non permite un interesado "Sin documento"',NULL,NULL);

COMMIT;


-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.01 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.02 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.02 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.03 --------------------------
-----------------------------------------------------------------------



-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.03 --------------------------
-----------------------------------------------------------------------

-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.04 --------------------------
-----------------------------------------------------------------------

-----------------------------------------------------------------------

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'msjDemasiadosTerceros',1,'Se han recuperado demasiados terceros. Es necesario refinar más la búsqueda',NULL,NULL);		
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (3,'msjDemasiadosTerceros',1,'Se han recuperado demasiados terceros. Es necesario refinar más la búsqueda',NULL,NULL);		
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msjDemasiadosTerceros',1,'Se han recuperado demasiados terceros. Es necesario refinar más la búsqueda',NULL,NULL);		

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'msjDemasiadosTerceros',2,'Atopáronse demasiados terceiros. É necesario restrinxir mais a búsqueda',NULL,NULL);		
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (3,'msjDemasiadosTerceros',2,'Atopáronse demasiados terceiros. É necesario restrinxir mais a búsqueda',NULL,NULL);		
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (5,'msjDemasiadosTerceros',2,'Atopáronse demasiados terceiros. É necesario restrinxir mais a búsqueda',NULL,NULL);		


-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.04 --------------------------
-----------------------------------------------------------------------

-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.05 --------------------------
-----------------------------------------------------------------------

-----------------------------------------------------------------------

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.05 --------------------------
-----------------------------------------------------------------------

-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.06 --------------------------
-----------------------------------------------------------------------

-----------------------------------------------------------------------



insert into a_tex values (1,'etiqDatosSga',1,'Datos SGA',null,null);
insert into a_tex values (1,'etiqExpedienteSga',1,'Expediente : ',null,null);
insert into a_tex values (1,'etiqCodigoSga',1,'Código : ',null,null);


 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.06 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.07 --------------------------
-----------------------------------------------------------------------


insert into a_tex values (1,'numResultados',1,'Nº Resultados Totales:',null,null);
insert into a_tex values (3,'numResultados',1,'Nº Resultados Totales:',null,null);
insert into a_tex values (4,'numResultados',1,'Nº Resultados Totales:',null,null);


-- tarea 120581

insert into a_tex values (4,'gbRecuperar',1,'Recuperar',null,null);
insert into a_tex values (4,'gbRecuperar',2,'Recuperar',null,null);
insert into a_tex values (4,'toolTip_bRecuperar',1,'Recuperar',null,null);
insert into a_tex values (4,'toolTip_bRecuperar',2,'Recuperar',null,null);
insert into a_tex values (4,'gEtiq_estado',1,'Estado',null,null);
insert into a_tex values (4,'gEtiq_estado',2,'Estado',null,null);
insert into a_tex values (4,'gEtiq_fecEstado',1,'Fecha',null,null);
insert into a_tex values (4,'gEtiq_fecEstado',2,'Fecha',null,null);
insert into a_tex values (4,'gEtiq_TipoDocNoExst1',1,'Tipo de documento: ',null,null);
insert into a_tex values (4,'gEtiq_TipoDocNoExst1',2,'Tipo de documento: ',null,null);
insert into a_tex values (4,'gEtiq_TipoDocNoExst2',1,' causo baja. Seleccione uno nuevo ',null,null);
insert into a_tex values (4,'gEtiq_TipoDocNoExst2',2,' causou baixa. Seleccione un novo ',null,null);




 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.07 --------------------------
-----------------------------------------------------------------------




-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.08 --------------------------
-----------------------------------------------------------------------


--Tarea #260881: Comprobaci?n existencia expediente en alta a instancia de parte
	
--Milagros Noya (19/01/2017)
	
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msgErrGenServ',1,'Ha ocurrido un error gen?rico en la comunicación cliente-servidor.',NULL,NULL);
	
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msgErrGenServ',2,'Produciuse un erro xen?rico na comunicación cliente-servidor.',NULL,NULL);
	
commit;



 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.08 --------------------------
-----------------------------------------------------------------------




-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.09 --------------------------
-----------------------------------------------------------------------

-- Tarea #209727: [CONS] Información movimientos expediente
-- Adrián (07/12/2015)
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqOperaciones',1,'Operaciones',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'tit_listMovExp',1,'Listado operaciones expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gEtiqTipoMovimiento',1,'Tipo movimiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gEtiqFecOpe',1,'Fecha operación',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'tit_detMovExp',1,'Detalles del movimiento',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntAlt',1,'Alta de interesado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntModificar',1,'Modificación de interesado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntEliminar',1,'Eliminación de interesado',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCodTer',1,'Código tercero',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntVerTer',1,'Versión tercero',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCodDom',1,'Cod. domicilio',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntNom',1,'Nombre',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntApel1',1,'Primer apellido',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntApel2',1,'Segundo apellido',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntTipoDoc',1,'Tipo documento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntDoc',1,'Documento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntTel',1,'Telefono',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntEmail',1,'Email',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntTipoVia',1,'Tipo via',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntNomVia',1,'Nombre via',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntEmp',1,'Emplazamiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntMun',1,'Municipio',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntPro',1,'Provincia',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambVer',1,'Cambio versión',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambRol',1,'Cambio rol',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambNotElect',1,'Cambio notificación electronica',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambDom',1,'Cambio domicilio',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambDatPer',1,'Cambio datos personales',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntVerA',1,'Versión antigua',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntVerN',1,'Versión nueva',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCodRol',1,'Código rol',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCodRolA',1,'Código rol antiguo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntDesRolA',1,'Rol antiguo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCodRolN',1,'Código rol nuevo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntDesRolN',1,'Rol nuevo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambDomA',1,'Domicilio antiguo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambDomN',1,'Domicilio nuevo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntPortal',1,'Portal',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntPlanta',1,'Planta',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntPuerta',1,'Puerta',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntEsc',1,'Escalera',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntBloque',1,'Bloque',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntLetDesde',1,'Letra desde',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntletHasta',1,'Letra hasta',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntNumDesde',1,'Numero desde',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntNumHasta',1,'Numero hasta',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCodPos',1,'Código postal',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntNotifElec',1,'Notificación electrónica',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntNotElectA',1,'Notificación electrónica antigua',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntNotElectN',1,'Notificación electrónica nueva',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambDatPerA',1,'Datos personales antiguos',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambDatPerN',1,'Datos personales nuevos',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltaExp',1,'Alta de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpNumExp',1,'Número de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCodMun',1,'Código de organización',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCodProc',1,'Código de procedimiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpEjercicio',1,'Ejercicio',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpUsuario',1,'Código de usuario',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpNomUsuario',1,'Nombre de usuario',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpNomUOR',1,'Nombre de unidad orgánica',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCodUOR',1,'Código de unidad orgánica',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAsunto',1,'Asunto',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpObservac',1,'Observaciones',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCodTramIni',1,'Trámite de inicio',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpProcAsiento',1,'Procedimiento asiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpMunAsiento',1,'Municipio asiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpTipoAsiento',1,'Tipo asiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAnoAsiento',1,'Año asiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpNumAsiento',1,'Número asiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCodUORAsiento',1,'Código de unidad orgánica asiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCodDepAsiento',1,'Departamento asiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpGrabarExp',1,'Grabación de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpLocalizac',1,'Localización',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCodLocal',1,'Código localización',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpReabrirExp',1,'Reapertura de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpEstado',1,'Estado expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpFinalizaExp',1,'Finalización de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAnulaExp',1,'Anulación de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpGrabarTram',1,'Grabación de trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCodTramite',1,'Código de trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpNomTramite',1,'Nombre trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpOcurrTramite',1,'Ocurrencia de trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpFecIni',1,'Fecha de inicio',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpFecFin',1,'Fecha de fin',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpFecIniPlz',1,'Fecha de inicio de plazo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpFecFinPlz',1,'Fecha de fin de plazo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpFecLimite',1,'Fecha límite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpBloqueo',1,'Bloqueo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpFinalizarTram',1,'Finalización de trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpResol',1,'Resolución trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpRetrocTram',1,'Retroceso de trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpExpIni',1,'Expediente inicial',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpExpRel',1,'Expediente relacionado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAnhadirRel',1,'Adición de expediente relacionado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpEliminarRel',1,'Eliminación de expediente relacionado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpDatosSupl',1,'Datos suplementarios de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpDatosSuplTr',1,'Datos suplementarios de trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpUsuFec',1,'Usuario y fecha operación',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpTipoAlta',1,'Tipo de alta de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltaNormal',1,'Alta convencional',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltaAsiento',1,'Alta desde asiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltaCopia',1,'Copia de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltaAsociado',1,'Alta como expediente asociado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltaWebsFlexia',1,'Alta desde webservice de Flexia',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpNumExpOr',1,'Número de expediente original',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCodDoc',1,'Código del documento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpNomDoc',1,'Nombe del documento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpExtDoc',1,'Extensión del documento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpMimeDoc',1,'Tipo mime del documento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpOriDoc',1,'Origen del documento',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltDocExp',1,'Alta de documento de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpElimDocExp',1,'Eliminación de documento de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltDocExt',1,'Alta de documento externo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpElimDocExt',1,'Eliminación de documento externo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltDocTra',1,'Alta de documento de tramitación',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpElimDocTra',1,'Eliminación de documento de tramitación',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpOperWSTram',1,'Operación realizada por servicio web de tramitación',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntEstado',1,'Estado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntCambioDom',1,'Cambio de domicilio de interesado',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAltDocSupTra',1,'Alta de documento como campo suplementario de trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpGrabarSupExp',1,'Grabación campos suplementarios de expediente',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpGrabarSupTra',1,'Grabación campos suplementarios de trámite',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjErrorInterno',1,'Error interno de la aplicación',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpMotivo',1,'Motivo de la finalización',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpPersonaAutoriza',1,'Persona que autoriza',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIniciarTramManual',1,'Inicialización de trámite manual',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIniciarTram',1,'Inicialización de trámite',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpRetrocTramOrigen',1,'Retroceso de trámite de origen',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpBloquearTram',1,'Bloqueo de trámite',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpDesbloquearTram',1,'Desbloqueo de trámite',NULL,NULL);

commit;

-- Registro de accesos a módulos Flexia (Básica)
CREATE TABLE "AUDITORIA_ACCESO_MODULOS" 
   ("ID" NUMBER(20,0) NOT NULL ENABLE, 	
	"ID_ORGANIZACION" NUMBER(1,0), 
	"ID_USUARIO" NUMBER(4,0) NOT NULL ENABLE, 
	"ID_APLICACION" NUMBER(3,0), 
	"FECHA_ACCESO" TIMESTAMP NOT NULL ENABLE
 );	
  
CREATE UNIQUE INDEX "PK_AUDITORIA_ACCESO_MODULOS" ON "AUDITORIA_ACCESO_MODULOS" ("ID");

CREATE SEQUENCE SEQ_AUDITORIA_ACCESO_MODULOS  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1;

--Español
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_Movimiento',1,'Movimiento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_Usuario',1,'Usuario',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_FechaHora',1,'Fecha y Hora',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_AccesoA',1,'Acceso a',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_Login',1,'Login',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_InfoAccesoModulo',1,'Accesos a módulos de Flexia',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'errorCargarAccesosModulos',1,'Se ha producido un error al recuperar el listado de accesos a los módulos',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'msjCriterioFechaInicioFin',1,'La fecha de inicio no puede ser inferior a la fecha de fin',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiqFechaUltimoAcceso',1,'Fecha último acceso',NULL,NULL);
--Gallego
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_Movimiento',2,'Movemento',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_Usuario',2,'Usuario',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_FechaHora',2,'Data e Hora',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_AccesoA',2,'Acceso a',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_Login',2,'Login',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiq_InfoAccesoModulo',2,'Accesos a módulos de Flexia',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'errorCargarAccesosModulos',2,'Produciuse un erro ao recuperar a listaxe de accesos aos módulos',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'msjCriterioFechaInicioFin',2,'A data de inicio non pode ser inferior a data de fin',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'gEtiqFechaUltimoAcceso',2,'Data último acceso',NULL,NULL);
-- FIN Registro de accesos a módulos Flexia (Básica)



insert into a_tex values (1, 'etiqBloquearDestino',1,'Bloquear Unidad de Destino', null, null);



 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.09 --------------------------
-----------------------------------------------------------------------




-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.10 --------------------------
-----------------------------------------------------------------------


-- Tarea #276459: Selecci?n de registros de salida en impresi?n de libro de registro

-- (15/05/2017)

INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'msjListadoLibro',1,'Mostrar Listado Libro Registro',NULL,NULL);

COMMIT;



 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.10 --------------------------
-----------------------------------------------------------------------




-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.11 --------------------------
-----------------------------------------------------------------------


 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.11 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.12 --------------------------
-----------------------------------------------------------------------

--Tarea #282054: Visualizar lista documentos aportados en interfaz registro de ficha expediente y en lista documentacion
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'gEtiqOrgano',1,'Órgano',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'gEtiqFechaEntrega',1,'Fecha entrega',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqDocAportados',1,'Documentos aportados anteriormente',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqDocAportadosEntrada',1,'Documentos aportados anteriormente a registro',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqDocAportadosSalida',1,'Documentos aportados anteriormente desde registro',NULL,NULL);

--Tarea #281562: Notificaciones fin plazo trámite - Envío correos a usuario que inició el trámite o el expediente
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'not_usu_inicio_tramite',1,'Notificar por correo electrónico al usuario que inició el trámite',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'not_usu_inicio_expediente',1,'Notificar por correo electrónico al usuario que inició el expediente',NULL,NULL);

-- Tareas #278739: Lanbide - Registro - Ajustes entradas procedentes de otro registro
-- Milagros Noya (05/07/2017)
-- Castellano
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'res_EntRelEje',1,'Año Entrada relacionada',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'res_EntRelNum',1,'Número Entrada relacionada',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjRangoEjeAnotRelInvalido',1,'El año introducido no es válido. No puede ser mayor al año actual ni inferior al año actual menos 5 años.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjAnotEjerNumOblig',1,'Falta el año o numero de la entrada relacionada de origen para el tipo de entrada procedente de otro registro.',null,null);

-- Gallego
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'res_EntRelEje',2,'Ano Entrada relacionada',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'res_EntRelNum',2,'Número Entrada relacionada',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjRangoEjeAnotRelInvalido',2,'O ano introducido non é válido. Non pode ser maior ao ano actual nin inferior ao ano actual menos 5 anos.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjAnotEjerNumOblig',2,'Falta o ano ou o numero da entrada relacionada de orixe para o tipo de entrada procedente doutro rexistro.',null,null);


-- Tareas #279909: [CONS] Notificaciones en ficha de expediente
	
-- Milagros Noya (15/06/2017)
-- castellano
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqTramiteNotif',1,'Trámite notificado',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqTituloTramiteNotif',1,'Trámite con notificaciones (electronicas y/o postales)',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqTabTitTramitesNotificados',1,'Trámites notificados (con notificación no electrónica)',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjErrTramiteNotifNoIni',1,'El trámite seleccionado no se corresponde con ningún elemento de la lista de trámites iniciados',null,null);
-- gallego
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqTramiteNotif',2,'Trámite notificado',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqTituloTramiteNotif',2,'Trámite con notificacións (electronicas e/ou postais)',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqTabTitTramitesNotificados',2,'Trámites notificados (con notificación non electrónica)',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjErrTramiteNotifNoIni',2,'O trámite elixido non se corresponde con ningún elemento da lista de trámites iniciados',null,null);
COMMIT;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.12 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.13 --------------------------
-----------------------------------------------------------------------

-- Tarea #285405 Titulo de pantalla de cat?logo de definici?n de procedimientos
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'titCatalogoProc',1,'Catálogo procedimientos',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'titCatalogoProc',2,'Catálogo procedementos',NULL,NULL);



INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'infReg_rbUniTramExternaS',1,'a otra Administración',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'infReg_rbUniTramExternaE',1,'desde otra Administración',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'infReg_UniDestinoExterna',1,'Unidad Externa',NULL,NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'infReg_UniOrigenExterna',1,'Unidad Externa',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'infReg_rbUniTramExternaS',2,'a outra Administración',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'infReg_rbUniTramExternaE',2,'dende outra Administración',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'infReg_UniDestinoExterna',2,'Unidade Externa',NULL,NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'infReg_UniOrigenExterna',2,'Unidade Externa',NULL,NULL);





	
-- Tarea #283061
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqUsuTraFinPlazo',1,'al usuario que inició el trámite',NULL,NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqUsuExpFinPlazo',1,'al usuario que inició el expediente', NULL, NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqUORFinPlazo',1,'a la unidad tramitadora',NULL,NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgErrorFinPlazoDestinatarios',1,'Debe seleccionar destinatario/s para las notificaciones en función de fecha limite ', NULL, NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgErrorNotFuncionPlazo',1,'Debe seleccionar condiciones de notificación en función de fecha limite cuando selecciona un destinatario',NULL,NULL);
	
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqUsuTraFinPlazo',2,'o usuario que iniciou o trámite',NULL,NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqUsuExpFinPlazo',2,'o usuario que iniciou o expediente', NULL, NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqUORFinPlazo',2,'a unidade tramitadora',NULL,NULL);
	
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgErrorFinPlazoDestinatarios',2,'Ten que marcar destinatario/s para as notificacións en función da data limite', NULL, NULL);	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgErrorNotFuncionPlazo',2,'Ten que marcar condicións de notificación en función da data limite o seleccionar un destinatario',NULL,NULL);



INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'msjNoRecuperaValor',1,' El valor seleccionado ya se encuentra dado de alta ',NULL,NULL);

INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'gbRecuperar',1,'Recuperar',NULL,NULL);
	
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'msjNoRecuperaValor',2,' O valor seleccionado xa se atopa dado de alta ',NULL,NULL);
	
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'gbRecuperar',2,'Recuperar',NULL,NULL);


	


COMMIT;




 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.13 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.14 --------------------------
-----------------------------------------------------------------------

-- Tareas #263634: Correcciones Flexia V16
	
-- Milagros Noya (07/09/2017)

update a_tex set tex_des='No se ha podido eliminar el cargo porque se han encontrado trámites asociados' where tex_cod='msjCargoTieneReg' and tex_idi=1;
update a_tex set tex_des='Non se puido eliminar o cargo porque se atoparon trámites asociados' where tex_cod='msjCargoTieneReg' and tex_idi=2;
commit;



-- Tarea #285202 Lanbide - Registro - Mantenimiento de asuntos - A?adir columnas con datos de procedimiento - Exportar a Excel

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gbExportarExcel',1,'Exportar',NULL,NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqCodProc',1,'Código Procedimiento',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqDesProc',1,'Descripción Procedimiento',NULL,NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'toolTip_Exportar',1,'Exportar Excel',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gbExportarExcel',2,'Exportar Excel',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqCodProc',2,'Código Procedemento',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqDesProc',2,'Descripción Procedemento',NULL,NULL);
	
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'toolTip_Exportar',2,'Exportar Excel',NULL,NULL);


-- Tareas #282307: Lanbide - A?adir columna de asunto en buz?n de entradas

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gEtiq_CodAsunto',1,'Código asunto',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gEtiq_CodAsunto',2,'Código asunto',NULL,NULL);

-- FIN Tareas #282307: Lanbide - A?adir columna de asunto en buz?n de entradas





 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.14 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.15 --------------------------
-----------------------------------------------------------------------



 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.15 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.16 --------------------------
-----------------------------------------------------------------------


insert into a_tex values (1,'msjAltaTramNum',1,'Se ha generado correctamente una anotación de salida con número',null,null);
insert into a_tex values (1,'msjAltaTramNum',2,'Xerouse correctamente unha anotación de saída con número',null,null);

insert into a_tex values (1,'msjAltaTramDia',1,'el día',null,null);
insert into a_tex values (1,'msjAltaTramDia',2,'o día',null,null);

insert into a_tex values (1,'msjAltaTramHora',1,'a las',null,null);
insert into a_tex values (1,'msjAltaTramHora',2,'ás',null,null);


 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.16 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.17 --------------------------
-----------------------------------------------------------------------

-- Tareas #296890	Lanbide - Marca de expediente con notificación telemática en pestaña Notificaciones

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'gEtiq_expNofifTelematica',1,'Expediente con notificación telemática',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'gEtiq_ubicacionDocu',1,'Ubicación Documentación',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'gEtiq_notifElectronicas',1,'Notif. electrónicas',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'gEtiq_expNofifTelematica',2,'Expediente con notificación telemática',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'gEtiq_ubicacionDocu',2,'Ubicación Documentación',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'gEtiq_notifElectronicas',2,'Notif. electrónicas',NULL,NULL);

-- FIN Tareas #296890

-- Tareas #288821: Registro - Impresión de justificantes de registro desde listado de consulta de registros


Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msgErrorNoJustifActivo',1,'No existe ninguna plantilla activa. No se puede generar el justificante de registro',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'etiq_btnImprJustif',1,'Imprimir Justificante',null,null);

-- FIN Tareas #288821


-- Tareas #294209  [CONS] Buzón de entrada Exportación a Excel	

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgErrorAnotacionSel',1,'No ha seleccionado ninguna anotación',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgExportarTodoLista',1,'?Desea exportar todo el listado?',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgErrorNoResultadosExportar',1,'No han encontrado resultados para exportar',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgErrorAnotacionSel',2,'Non seleccionou ningunha anotación',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgExportarTodoLista',2,'?Desexa exportar todo o listado?',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgErrorNoResultadosExportar',2,'Non se atoparon resultados para exportar',NULL,NULL);

-- Tarea #297244	Ficha expediente - Notificaciones - Añadir columnas fecha solicitud envío e identificador de notificación
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqFechaSolEnvio',1,'Fecha sol Envío',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqCodNotificacion',1,'Identificador',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqFechaSolEnvio',2,'Fecha sol Envío',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqCodNotificacion',2,'Identificador',NULL,NULL);


-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.17 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.18 --------------------------
-----------------------------------------------------------------------

-- Tarea SSR: 275688

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_Documento',1,'Documento',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_Documento',2,'Documento',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiq_si',1,'Si',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiq_si',2,'Si',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiq_no',1,'No',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiq_no',2,'Non',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqTitDoc',1,'Descripci?n del fichero',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqTitDoc',2,'Descrici?n do ficheiro',null,null);	
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqRutaFichero',1,'Ruta del fichero.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqRutaFichero',2,'Ruta do ficheiro.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqRecuperadoScan',1,'El documento ha sido recuperado correctamente del esc?ner.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqRecuperadoScan',2,'O documento foi recuperado correctamente do esc?ner.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'advModifDoc',1,'Ya existe un fichero subido. Si desea modificarlo incorpore uno nuevo',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'advModifDoc',2,'Xa existe un fichero subido. Se desexa modificalo incorpore un novo',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gbAceptar',1,'Aceptar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gbAceptar',2,'Aceptar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gbCancelar',1,'Cancelar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gbCancelar',2,'Cancelar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gbDescargar',1,'Descargar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gbDescargar',2,'Descargar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gbAdjuntar',1,'Adjuntar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gbAdjuntar',2,'Adxuntar',null,null);	
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqExtensionInvalid',1,'S?lo son v?lidos ficheros de tipo: ',null,null);	
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqExtensionInvalid',2,'S? son v?lidos ficheiros de tipo: ',null,null);


insert into a_tex values (4,'msgExtensionNoCoincide',1,'La extensión del fichero no coincide con la del nombre. ¿Desea cambiarla?',null,null);




Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'btnVerEtiquetas',1,'Ver Etiquetas',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'btnVerEtiquetas',2,'Ver Etiquetas',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'titRelEtiquetas',1,'Relaci?n de etiquetas',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'titRelEtiquetas',2,'Relaci?n de etiquetas',null,null);
	
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msgErrSinEtiquetas',1,'No hay etiquetas que mostrar.',null,null);
	
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msgErrSinEtiquetas',2,'Non hai etiquetas que amosar.',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msgErrObtenerEtiquetas',1,'Ha ocurrido un error al recuperar los datos.',null,null);	
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msgErrObtenerEtiquetas',2,'Produciuse un erro ? obter os datos.',null,null);	
commit;




-- Tarea #275367: [CONS] Nuevo tipo de plantillas. Listado de etiquetas
-- Milagros Noya (11/05/2017)

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'btnVerEtiquetas',1,'Ver Etiquetas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'btnVerEtiquetas',2,'Ver Etiquetas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'titRelEtiquetas',1,'Relación de etiquetas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'titRelEtiquetas',2,'Relación de etiquetas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msgErrSinEtiquetas',1,'No hay etiquetas que mostrar.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msgErrSinEtiquetas',2,'Non hai etiquetas que amosar.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msgErrObtenerEtiquetas',1,'Ha ocurrido un error al recuperar los datos.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msgErrObtenerEtiquetas',2,'Produciuse un erro ó obter os datos.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msgErrGenServ',1,'Ha ocurrido un error genérico en la comunicación cliente-servidor.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msgErrGenServ',2,'Produciuse un erro xenérico na comunicación cliente-servidor.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msjCargDatos',1,'Cargando datos ... Espere un momento, por favor.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msjCargDatos',2,'Cargando datos ... Espere un momento, por favor.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msjEtiquetaObligatorios',1,'Los campos Procedimiento y Trámite son obligatorios.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msjEtiquetaObligatorios',2,'Os campos Procedemento e Trámite son obrigatorios.',null,null);
commit;




Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_nombre',1,'Nombre',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_etiqueta',1,'Etiqueta',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_plantillaBloqueIntODT1',1,'Las plantillas POR INTERESADO contendrán el siguiente bloque al inicio del documento:',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_plantillaBloqueIntODT2',1,'[#list LISTAINTERESADOS as item]',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_plantillaBloqueIntODT3',1,'Y al final del documento:',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_plantillaBloqueIntODT4',1,'[/#list]',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_plantillaBloqueODT',1,'Las etiquetas de interesado tendrán que ir dentro del bloque:',null,null);




Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjTramGrabarAutomat',1,'El trámite se grabará automáticamente',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjExpGrabarAutomat',1,'El expediente se grabará automáticamente',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjNoModificarJustificanteCSV',1,'No se puede modificar el justificante',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjNoModificarJustificanteCSV',2,'Non se pode modificar o xustificante',null,null);


Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjTramGrabarAutomat',1,'El trámite se grabará automáticamente',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjExpGrabarAutomat',1,'El expediente se grabará automáticamente',null,null);



insert into a_tex values (4,'btnVisibleExt',1,'Visible Ext.' ,null, null);

insert into a_tex values (6,'btnVisibleExt',1,'Visible Ext.', null, null);

insert into a_tex values (4,'visibleAppExt',1,'Visible App. Externas', null, null);
insert into a_tex values (6,'visibleAppExt',1,'Visible App. Externas', null, null);


Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msgErrGenServ',1,'Ha ocurrido un error gen?rico en la comunicaci?n cliente-servidor.',null,null);
	
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msgErrGenServ',2,'Produciuse un erro xen?rico na comunicaci?n cliente-servidor.',null,null);

COMMIT;


Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (20,'errNoExisteUsuarioFirmaFlujo',1,'No existe el usuario determinado en la importación ',null,null);
	
COMMIT;


Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gbCSV',1,'CSV',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjCrearCSV',1,'¿Desea generar el código seguro de verificación?',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjCSVGeneradoOK',1,'El código seguro de verificación se ha generado correctamente',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'errorCrearCSVDoc',1,'No se ha podido generar el código seguro de verificación del documento',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'errorCSVDocYaExiste',1,'El documento ya tiene un código seguro de verificación',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'errorCSVFormatoNoSoportado',1,'Este documento es de un tipo no soportado',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'errCodigoDesconocidoCampoSup',1,'Se desconoce el código del documento',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqNoEditable',1,'NO EDITABLE',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjNoEditable',1,'Este documento no se puede modificar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjNoModificarJustificanteCSV',1,'No se puede modificar el justificante',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'errNombreIgualJustificanteCSV',1,'El nombre de fichero está reservado para los justificantes, no se puede utilizar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'etiqGeneracionJustificante',1,'GENERACION DE JUSTIFICANTE',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'histJustificante',1,'Generación del justificante con estos datos',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'histNombre',1,'Nombre',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'histCSV',1,'CSV',null,null);

--Gallego
/*Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gbCSV',2,'CSV',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjCrearCSV',2,'¿Desexa xerar o código seguro de verificación?',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjCSVGeneradoOK',2,'O código seguro de verificación xerouse correctamente',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'errorCrearCSVDoc',2,'Non se puido xerar o código seguro de verificación do documento',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'errorCSVDocYaExiste',2,'O documento xa ten un código seguro de verificación',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'errorCSVFormatoNoSoportado',2,'Este documento é dun tipo non soportado',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'errCodigoDesconocidoCampoSup',2,'Descoñécese o código do documento',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqNoEditable',2,'NO EDITABLE',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjNoEditable',2,'Este documento non se pode modificar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjNoModificarJustificanteCSV',2,'Non se pode modificar o xustificante',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'errNombreIgualJustificanteCSV',2,'O nome de ficheiro está reservado para os xustificantes, non se pode utilizar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'etiqGeneracionJustificante',2,'XERACION DE XUSTIFICANTE',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'histJustificante',2,'Xeración do xustificante con estos datos',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'histNombre',2,'Nome',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'histFecha',2,'Data',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'histCSV',2,'CSV',null,null);*/


-- Tarea #269406: [CONS] Integración con Port@firmas
-- Jorge Garcia (31/08/2017)

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gbActivarDesactivar',1,'Activar/Desactivar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gbDefinirFlujo',1,'Definir',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gEtiqMantFlujoFirmas',1,'Mantenimiento de flujos de firma',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gEtiqMantCircuitoFirmas',1,'Circuito de firmas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqFlujo',1,'Flujo',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'preguntaEliminarFlujoFirma',1,'¿Desea eliminar ese flujo de firmas?',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'preguntaElimUserCircuitoFirma',1,'¿Desea eliminar el usuario de este circuito de firmas?',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjExisteNombreFlujoFirma',1,'Ya existe un flujo de firma con el mismo nombre',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjUsuarioYaExiste',1,'El usuario seleccionado ya está incluido en el circuito de firmas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'titSelUsuario',1,'Seleccione un usuario',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gEtiq_NIF',1,'NIF',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjUsuarioNoExistePortafirmas',1,'El usuario seleccionado no existe en Port@firmas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'titSelFlujo',1,'Seleccione un flujo de firmas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqNombres',1,'Nombres',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqModoFirma',1,'Modo de Firma',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjTipoFlujoFirmaObligatorio',1,'El modo de firma es obligatorio. Debe escoger uno de los valores.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjCircuitoFirmaUsuarioOblig',1,'El circuito de firmas debe tener al menos un usuario asignado',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gEtiqEstadoCircuitoFirmas',1,'Estado del circuito de firmas',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqVerDatosFirma',1,'Ver datos de la firma',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'titDetalleEstadoFirma',1,'Detalle del estado de la firma',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqFirmante',1,'Firmante',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjNoElimDocEnProcesoFirma',1,'No se puede eliminar un documento en proceso de firma.',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'ErrCargarApplet',1,'Ha ocurrido un error al cargar el componente de firma.',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'ErrCargarAppletAutoFirma',1,'Ha ocurrido un error al cargar el componente de Autofirma.',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'ErrDescargarFicheroAFirmar',1,'Ha ocurrido un error al intentar descargar el documento a firmar.',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjErrorFirmarDocumento',1,'Se ha detectado un error al intentar firmar el documento',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjDocLocalFirmadoOk',1,'El documento ha sido firmado correctamente en su equipo. Pulse Aceptar para guardar la firma definitivamente en el servidor.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjDocLocalNoFirmado',1,'El documento no se ha firmado.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjNoModifDocEnProcesoFirma',1,'No se puede modificar un documento en proceso de firma.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'msjNoModifDocRechazado',1,'No se puede modificar un documento rechazado. La única opción es visualizarlo.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'gEtiq_firma',1,'Firma',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'titSelFlujo',1,'Seleccione un flujo de firmas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqNombres',1,'Nombres',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqFlujo',1,'Flujo',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'msjUsuarioNoExistePortafirmas',1,'El usuario seleccionado no existe en Port@firmas',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (11,'msjCargDatos',1,'Cargando datos ... Espere un momento, por favor.',NULL,NULL);

-- Textos que faltan para los modulos de Registro de entrada y Registro de salida
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'errModifAsuEliminado',1,'No se puede modificar un asunto dado de baja',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (9,'msgAsunYaEliminado',1,'El asunto ya ha sido dado de baja',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (997,'errModifAsuEliminado',1,'No se puede modificar un asunto dado de baja',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (997,'msgAsunYaEliminado',1,'El asunto ya ha sido dado de baja',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (998,'errModifAsuEliminado',1,'No se puede modificar un asunto dado de baja',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (998,'msgAsunYaEliminado',1,'El asunto ya ha sido dado de baja',null,null);



insert into a_tex values (4,'msjUsuarioNoAnhadirFlujo',1,'El usuario no puede ser añadido al flujo',null,null);
insert into a_tex values (6,'msjUsuarioNoAnhadirFlujo',1,'El usuario no puede ser añadido al flujo',null,null);

COMMIT;

-- Tarea #296240
-- retroceder un trámite con documentos pendientes de firma

insert into a_tex values (4,'msjRetrocederPendienteFirma',1,'Va a retroceder un trámite con documentos pendientes de firma. ¿Desea continuar?',null,null);
COMMIT;


-- Tarea #302601 - Lanbide - Buzón de entrada - Alta de expediente con el año del registro que lo inicia
-- Mila Noya (06/02/2018)
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiqExpAnoAsiento',1,'Numeración según anotación ',null,null);
COMMIT;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.18 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.19 --------------------------
-----------------------------------------------------------------------

-- Tarea #120581: [CONS] Desactivación tipos documento registro en Mantenimiento de Registro

INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gbRecuperar',1,'Recuperar',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gbRecuperar',2,'Recuperar',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'toolTip_bRecuperar',1,'Recuperar',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'toolTip_bRecuperar',2,'Recuperar',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gEtiq_estado',1,'Estado',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gEtiq_estado',2,'Estado',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gEtiq_fecEstado',1,'Fecha',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gEtiq_fecEstado',2,'Fecha',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gEtiq_TipoDocNoExst1',1,'Tipo de documento: ',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gEtiq_TipoDocNoExst1',2,'Tipo de documento: ',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gEtiq_TipoDocNoExst2',1,' causo baja. Seleccione uno nuevo ',NULL,NULL);
INSERT INTO  A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'gEtiq_TipoDocNoExst2',2,' causou baixa. Seleccione un novo ',NULL,NULL);

COMMIT;

-- Tarea #270046: Registro - DOKUSI - Integración componente digitalización documentos en REGEXLAN

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjErrEliminarDocDigit',1,'No se puede eliminar un documento añadido a través del componente de digitalización',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjErrEliminarDocDigit',2,'Non se pode eliminar un documento que foi engadido desde o compoñente de dixitalización',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjErrModificarDocDigit',1,'No se puede modificar un documento añadido a través del componente de digitalización',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjErrModificarDocDigit',2,'Non se pode modificar un documento que foi engadido desde o compoñente de dixitalización',null,null);
COMMIT;

-- Tarea #287835: Lanbide - Marca de digitalización de documentos de registro por procedimiento

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (9,'msgConfirmDig',1,'¿Desea modificar la opción de digitalizacion documento en registro?',null,to_date('27/03/2017','DD/MM/YYYY'));
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (9,'etiqDigitalizacion',1,'Digitalización documento registro',null,null);
COMMIT;

-- Tareas #291109 [CONS] Gestión de entradas rechazadas

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqMisRechazadas',1,'Mis Entradas Rechazadas',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'rotulo_usuRechazo',1,'Usuario Rechazo',NULL,NULL);
COMMIT;

--Tarea #300045: Compulsa - Registro - Botón de fin de digitalización de documentos
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'gbFinDigitalizar',1,'Fin. digit.',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'altFinDigitalizar',1,'Finalizar la digitalización',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'msjGrabarAntesFinDig',1,'Se finalizará la digitalización de documentos, ¿desea continuar?',NULL,NULL);
COMMIT;

-- Tareas #291976: Compulsa - Registro - Botón de catalogación de documentos
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gbTodosTipos',1,'Todos los tipos',null,null);
COMMIT;

--Tarea #300045: Compulsa - Registro - Botón de fin de digitalización de documentos
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'gbFinalizar',1,'Finalizar',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'altFinalizar',1,'Finalizar registro',NULL,NULL);
COMMIT;


-- Tareas #304936: Compulsa - No permitir impresión de justificante de registro en registros pendientes de fin digitalización (PFD)
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values 
(1,'noImprimirJustificante',1,'No es posible imprimir el justificante de registro en esta entrada porque está pendiente de finalizar el proceso de digitalización de documentos',NULL,NULL);
COMMIT;

-- Tareas #300069 Compulsa - Documentación asociada al expediente - Documentación aportada desde registro - Añadir dato de fecha entrada y tipo documental

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'btnCatalogar',1,'Catalogar',NULL,NULL);
COMMIT;

-- Tareas #291109 [CONS] Gestión de entradas rechazadas y registros pendientes de fin de digitalización (PFD)

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqueta_asunto',1,'Asunto',NULL,NULL);

-- etiquetas nuevas desarrollo digitalización
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqTipoDocumental',1,'Tipo Documental',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqCatalogacion',1,'Catalogación',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqCatalogado',1,'Catalogado',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqCompulsado',1,'Compulsado',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqTipificar',1,'Tipificar',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqAbrirDoc',1,'Abrir Documento',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'etiqValor',1,'Valor',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'errorConExt',1,'Ha ocurrido un error con la conexión a aplicación externa',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'errorTipificar',1,'Ha ocurrido un error al Tipificar el documento',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'errorMetadatos',1,'Ha ocurrido un error al recuperar los metadatos del tipo documental seleccionado',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'errorOper',1,'La operación ha fallado',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'errorTipDoc',1,'Ha ocurrido un error al recuperar los tipos documentales',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'errorCatalogacion',1,'Ha ocurrido un error al recuperar la catalogación del documento',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'errorBBDD',1,'Error al obtener una conexión a la BBDD',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgIndicarMetadatos',1,'Debe indicar el valor para, al menos, un metadato',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgMetadatosOblig',1,'Debe indicar el valor para todos los metadatos obligatorios',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msgTipoDocumental',1,'Debe seleccionar un Tipo Documental',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqTipoDocumental',1,'Tipo Documental',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqCompulsado',1,'Compulsado',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqEntRechazadas',1,'Entradas Rechazadas',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqPendFinDigit',1,'Entradas Pendientes Fin Digitalización',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'etiqFiltrosRech',1,'Filtros Entradas Rechazadas',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'errorNomInteresado',1,'Error al generar el nombre completo del interesado de una anotación de registro',NULL,NULL);

INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (1,'noImprimirJustificanteListado',1,'No es posible imprimir el justificante de registro, alguna de las entradas seleccionadas esta pendiente de finalizar el proceso de digitalización de documentos',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiq_obligC',1,'Oblig.',null,null);
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES (1, 'msgErrGenServ', 1, 'Ha ocurrido un error genérico en la comunicación cliente-servidor.', NULL, NULL);

COMMIT;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.19 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.20 --------------------------
----------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
VALUES (1,'tituloTerminarDigitalizar',1,'Fin digitalización de documentos',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
VALUES (1,'msgTerminarDigitalizar',1,'Cuando haya terminado la digitalización pulse el botón "Volver a registro".',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
VALUES (1,'gbVolverRegistro',1,'Volver a registro',NULL,NULL);
COMMIT;

-- Tareas #327660: [Lanbide] Compulsa - Interfaz intermedia de catalogación modal - Comprobación de entrada no finalizada antes de grabar los documentos
-- Mila noya (06/08/2018)
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'msgErrDigitAnotFinSI',1,'La ventana del componente de digitalización sigue abierta. Debe cerrarla para poder volver al registro.',null,null);
COMMIT;

-- Tareas #325617: [Lanbide] Compulsa y catalogación - desarrollos Altia Vitoria
    -- Tareas #323256: Compulsa - Mejoras en interfaz de catalogación (II)
    -- José Ignacio Zomosa (11/07/2018)
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'etiqObservDoc', 1, 'Observaciones');

UPDATE A_TEX SET TEX_DES = 'Catalogar' WHERE TEX_COD = 'etiqTipificar' AND TEX_IDI = 1;

INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'msgNoPDF', 1, 'El documento no está en formato PDF. <br>Para abrirlo pulse el botón \"Abrir Documento\".');

INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'msgNoMetadatos', 1, 'El tipo documental seleccionado no tiene metadatos.');

COMMIT;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.20 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.21 --------------------------
----------------------------------------------------------------------

-- Tareas #339115: [Lanbide] Histórico operaciones expediente - Incorporar nueva operación Cambio de Unidad
-- Milagros Noya (22/10/2018)

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpCambioUtr',1,'Cambio de unidad tramitadora',null,null);
COMMIT;

-- Tareas #333673: [Lanbide] Compulsa - Registro de operaciones de grabación de documentos compulsados y de finalización registro digitalizable
-- Elías Montes (26/10/2018)
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqDigitalizacionDocumentos',1,'DIGITALIZACION DE DOCUMENTOS',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqDigitalizacionDocumentos',2,'DIXITALIZACION DE DOCUMENTOS',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqFinalizacionDocumentos',1,'FINALIZACIÓN',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqFinalizacionDocumentos',2,'FINALIZACIÓN',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqFinalizacionModificacion',1,'FINALIZACIÓN MODIFICACIÓN',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqFinalizacionModificacion',2,'FINALIZACIÓN MODIFICACIÓN',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqIdDocGestDoc',1,'ID documento gestor documental',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqCodUndOrganica',1,'Código unidad orgánica',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqEjercicioAnotacion',1,'Ejercicio anotación',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqNumeroRegistro',1,'Numero de registro',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqIdDocGestDoc',2,'ID documento xestor documental',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqCodUndOrganica',2,'Código unidade orgánica',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqEjercicioAnotacion',2,'Ejercicio anotación',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqNumeroRegistro',2,'Numero de rexistro',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqNombreDocumento',1,'Nombre documento',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqNombreDocumento',2,'Nome documento',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqFinDigModif',1,'FIN DIGITALIZACIÓN EN MODIFICACIÓN',null,null);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC)
values (1,'etiqFinDigModif',2,'FIN DIXITALIZACIÓN EN MODIFICACIÓN',null,null);

COMMIT;

-- Tareas #320518 (0301218 Digitalización 2018): Compulsa - Mejoras en interfaz de catalogación 
-- José Ignacio Delgado (11/10/2018)
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'gEtiq_MasInfo', 1, 'Más información');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'gEtiq_MasInfo', 4, 'Más información_EU');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'gEtiq_TiposDoc', 1, 'Tipos Documentales');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'gEtiq_TiposDoc', 4, 'Tipos Documentales_EU');
COMMIT;

-- Tareas #320518 (0301218 Digitalización 2018): Compulsa - Mejoras en interfaz de catalogación 
-- Milagros Noya (12/11/2018)
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'gEtiq_tipDocGrupo', 1, 'Familia');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'gEtiq_tipDocId', 1, 'Id');
COMMIT;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.21 --------------------------
----------------------------------------------------------------------