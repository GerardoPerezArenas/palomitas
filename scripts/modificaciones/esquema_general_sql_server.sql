-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.00.00 --------------------------
-----------------------------------------------------------------------

-- Tareas #340541 [CONS] -ENI- Adaptacion y creacion de Script para SQLServer
-- Juan García
--El script añade el area PROCEDIMIENTO ENI y el tipo de procedimiento ENI para la creacion del procedimiento para la importacion de expedientes ENI.
-- Declaracion de variables
DECLARE        @codigoTipoPro AS int;
DECLARE        @codigoArea AS int;

-- Inicializacion de variables
SET @codigoTipoPro = ( SELECT MAX(TPR_COD) + 1 FROM A_TPR);
SET @codigoArea = (SELECT MAX(ARE_COD) + 1 FROM A_ARE);

-- AREAS -----------------------------------------------------------------------
INSERT INTO A_ARE (ARE_COD) VALUES (:codigoArea);
INSERT INTO A_AML (AML_COD,AML_CMP,AML_LENG,AML_VALOR) VALUES (@codigoTipoPro,'NOM','1','PROCEDIMIENTO ENI');
-- TIPOS PROCEDIMIENTO ---------------------------------------------------------
INSERT INTO A_TPR (TPR_COD) VALUES (@codigoTipoPro);
INSERT INTO A_TPML (TPML_COD, TPML_CMP, TPML_LENG, TPML_VALOR) VALUES (@codigoTipoPro,'NOM','1','ENI');
-- Traducciones ----------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'tituloImportarExp', 1, 'Importar expediente');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'tituloImportarExp', 2, 'Importar expediente');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'etiqBotonImportar', 1, 'Expediente a importar*');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'etiqBotonImportar', 2, 'Expediente a importar*');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'msjTipoEntradaImport', 1, '* El fichero de entrada debe ser un fichero comprimido en formato ZIP con la siguiente estructura:');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'msjTipoEntradaImport', 2, '* O ficheiro de entrada debe ser un ficheiro comprimido en formato ZIP coa seguinte estrutura:');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'txtIndiceImportar', 1, '<b>INDICE.XML</b>(contenido del ?ndice electr?nico)');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'txtIndiceImportar', 2, '<b>INDICE.XML</b>(contido do ?ndice electr?nico)');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'txtDocumentoImportar', 1, '<b>DOC_<idDocumento>.xml </b>(un xml por documento, idDocumento es el identificador contenido dentro de los metadatos ENI en el xml. Ejemplo:<br/>DOC_ES_E00010207_2010_MPR00000000000000000000000000010201.xml)');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'txtDocumentoImportar', 2, '<b>DOC_<idDocumento>.xml </b>(un xml por documento, idDocumento ? o identificador contido dentro dos metadatos ENI no xml. Exemplo:<br/>DOC_ES_E00010207_2010_MPR00000000000000000000000000010201.xml)');



--Tareas #332486 [CONS] -SIR- Creación de pantalla de buzón de entrada de asientos
-- Kevin Rañales
--Insertamos los textos de las pantalla del SIR.
--TITULOS ----------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titMotivoRechazo'              ,'1','Motivo Rechazo',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titMotivoRechazo'              ,'2','MotivoRechazo',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titBuzonEntradaIntercambio'    ,'1','Buzón De Entrada De Intercambios',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titBuzonEntradaIntercambio'    ,'2','Buzón De Entrada De Intercambios',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titInfoAsiento'                ,'1','Información Del Asiento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titInfoAsiento'                ,'2','Información Do Asento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titInfoInteresado'             ,'1','Información Del Interesado',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titInfoInteresado'             ,'2','Información Do Interesado',null,null);
--TITULOS TABLA ----------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaCodIntercambio'       ,'1','Código Intercambio',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaCodIntercambio'       ,'2','Código Intercambio',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaFechaEntrada'         ,'1','Fecha Entrada',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaFechaEntrada'         ,'2','Fecha Entrada',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaIdentUnidRegOrigen'   ,'1','Identificador Unidad Registro Origen',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaIdentUnidRegOrigen'   ,'2','Identificador Unidade Rexistro Orixen',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaDescAsunto'           ,'1','Descripción Asunto',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaDescAsunto'           ,'2','Descrición Asunto',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaNumExpediente'        ,'1','Número Expediente',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaNumExpediente'        ,'2','Número Expediente',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaCodTipoAnotacion'     ,'1','Código Tipo Anotación',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaCodTipoAnotacion'     ,'2','Código Tipo Anotación',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaListoParaEnviar'      ,'1','Listo Para Enviar',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaListoParaEnviar'      ,'2','Listo Para Enviar',null,null);
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaInfoAsiento'          ,'1','Información Del Asiento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaInfoAsiento'          ,'2','Información Do Asento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaInfoAdiAsiento'       ,'1','Información Adicional Del Asiento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaInfoAdiAsiento'       ,'2','Información Adicional Do Asento',null,null);
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaTipoDocIdent'         ,'1','Tipo Identificación Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaTipoDocIdent'         ,'2','Tipo Identificación Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaDocIdent'             ,'1','Identificación Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaDocIdent'             ,'2','Identificación Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaNombreORazon'         ,'1','Nombre/Razón Social',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaNombreORazon'         ,'2','Nome/Razón Social',null,null);
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaNomFichero'           ,'1','Nombre Fichero',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaNomFichero'           ,'2','Nome Ficheiro',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaIdFichero'            ,'1','Identificador Fichero',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaIdFichero'            ,'2','Identificador Ficheiro',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaValidezDocumento'     ,'1','Validez Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaValidezDocumento'     ,'2','Validez Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaTipoDocumento'        ,'1','Tipo Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaTipoDocumento'        ,'2','Tipo Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaFirmaDocumento'       ,'1','Firma Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaFirmaDocumento'       ,'2','Firma Documento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaTipoMime'             ,'1','Tipo MIME',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaTipoMime'             ,'2','Tipo MIME',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaIdDocumentoFirmado'   ,'1','Identificador Documento Firmado',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaIdDocumentoFirmado'   ,'2','Identificador Documento Firmado',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaObservaciones'        ,'1','Observaciones',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaObservaciones'        ,'2','Observacións',null,null);
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaDocumentoInteresado'  ,'1','Documento Del Interesado',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaDocumentoInteresado'  ,'2','Documento Do Interesado',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaDomicilioInteresado'  ,'1','Domicilio Del Interesado',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTablaDomicilioInteresado'  ,'2','Domicilio Do Interesado',null,null);
--ETIQUETAS --------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqAsiento'                   ,'1','Asiento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqAsiento'                   ,'2','Asento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqVerInteresadosYDomicilios' ,'1','Visualización de interesados y domicilios',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqVerInteresadosYDomicilios' ,'2','Visualización de interesados e domicilios',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqVerAnexos'                 ,'1','Ver Anexos',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqVerAnexos'                 ,'2','Ver Anexos',null,null);
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodAsiento'                ,'1','Código asiento:', NULL, NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodAsiento'                ,'2','Código asento:', NULL, NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodEntidadRegOrigen'       ,'1','Código entidad registral origen:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodEntidadRegOrigen'       ,'2','Código entidade rexistral orixe:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescEntidadRegOrigen'      ,'1','Descripción entidad registral origen:', NULL, NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescEntidadRegOrigen'      ,'2','Descrición entidade rexistral orixen:', NULL, NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodAsunto'                 ,'1','Código asunto:', NULL, NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodAsunto'                 ,'2','Código asunto:', NULL, NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodTipoAnot'               ,'1','Código tipo de anotación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodTipoAnot'               ,'2','Código tipo de anotación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTipoAnotacion'             ,'1','Tipo de anotación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqTipoAnotacion'             ,'2','Tipo de anotación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqNumExpediente'             ,'1','Número de expediente:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqNumExpediente'             ,'2','Número de expediente:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqFecEntrada'                ,'1','Fecha de entrada:', NULL, NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqFecEntrada'                ,'2','Fecha de entrada:', NULL, NULL);
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqNumRegEnt'                 ,'1','Número registro de entrada:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqNumRegEnt'                 ,'2','Número rexistro de entrada:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodUnidTramOrigen'         ,'1','Código unidad tramitadora origen:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodUnidTramOrigen'         ,'2','Código unidade tramitadora orixe:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescUnidTramOrigen'        ,'1','Descripción unidad tramitadora origen:', NULL, NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescUnidTramOrigen'        ,'2','Descripción unidade tramitadora orixe:', NULL, NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqNomUsuario'                ,'1','Nombre de usuario:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqNomUsuario'                ,'2','Nome de usuario:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodEntidadRegDestino'      ,'1','Código entidad registral destino:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodEntidadRegDestino'      ,'2','Código entidade rexistral destino:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescEntidadRegDestino'     ,'1','Descripción entidad registral destino:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescEntidadRegDestino'     ,'2','Descripción entidade rexistral destino:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqContactoUsuario'           ,'1','Contacto de usuario:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqContactoUsuario'           ,'2','Contacto de usuario:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodUnidTramDestino'        ,'1','Código unidad tramitadora destino:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodUnidTramDestino'        ,'2','Código unidade tramitadora destino:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescUnidTramDestino'       ,'1','Descripción unidad tramitadora destino:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescUnidTramDestino'       ,'2','Descripción unidade tramitadora destino:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodTipoTransporte'         ,'1','Código tipo de transporte:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodTipoTransporte'         ,'2','Código tipo de transporte:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqNumTransporte'             ,'1','Número de transporte:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqNumTransporte'             ,'2','Número de transporte:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodIntercambio'            ,'1','Código de intercambio:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodIntercambio'            ,'2','Código de intercambio:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqVersionAplicacion'         ,'1','Versión de la aplicación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqVersionAplicacion'         ,'2','Versión da aplicación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodTipoReg'                ,'1','Código tipo de registro:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodTipoReg'                ,'2','Código tipo de rexistro:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodDocFis'                 ,'1','Código documentación física:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodDocFis'                 ,'2','Código documentación física:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodEntidadRegInicio'       ,'1','Código entidad registral inicio:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqCodEntidadRegInicio'       ,'2','Código entidade rexistral inicio:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescEntidadRegInicio'      ,'1','Descripción entidad registral inicio:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDescEntidadRegInicio'      ,'2','Descripción entidade rexistral inicio:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqEstado'                    ,'1','Estado:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqEstado'                    ,'2','Estado:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqResumen'                   ,'1','Resumen:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqResumen'                   ,'2','Resumen:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqExpone'                    ,'1','Expone:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqExpone'                    ,'2','Expone:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqSolicita'                  ,'1','Solicita:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqSolicita'                  ,'2','Solicita:',null,null);
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoTipoDocId'       ,'1','Tipo de documento de identificación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoTipoDocId'       ,'2','Tipo de documento de identificación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoDocumentoId'     ,'1','Documento de identificación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoDocumentoId'     ,'2','Documento de identificación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoNombreRazon'     ,'1','Nombre/Razón social:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoNombreRazon'     ,'2','Nome/Razón social:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoCorreoElectr'    ,'1','Correo electrónico:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoCorreoElectr'    ,'2','Correo electrónico:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoTelefono'        ,'1','Teléfono:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoTelefono'        ,'2','Teléfono:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoDirElectrHab'    ,'1','Dirección electrónica habilitada:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoDirElectrHab'    ,'2','Dirección electrónica habilitada:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoCanalComuni'     ,'1','Canal preferente de comunicación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoCanalComuni'     ,'2','Canal preferente de comunicación:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoObservaciones'   ,'1','Observaciones:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqInteresadoObservaciones'   ,'2','Observacións:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioPais'             ,'1','País:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioPais'             ,'2','País:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioProvincia'        ,'1','Provincia:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioProvincia'        ,'2','Provincia:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioMunicipio'        ,'1','Municipio:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioMunicipio'        ,'2','Concello:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioDireccion'        ,'1','Dirección:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioDireccion'        ,'2','Enderezo:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioCodPostal'        ,'1','Código Postal:',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqDomicilioCodPostal'        ,'2','Código Postal:',null,null);
--BOTONES ----------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','gbVerInteresado'               ,'1','Ver interesado',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','gbVerInteresado'               ,'2','Ver interesado',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','gbVerAnexo'                    ,'1','Ver anexo',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','gbVerAnexo'                    ,'2','Ver anexo',null,null);
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqMotivoRechazo'             ,'1','Introducir Motivo Rechazo',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqMotivoRechazo'             ,'2','Introducir Motivo Rexeitamento',null,null);
--Mensajes----------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','msjFaltaMotivoRechazo'         ,'1','No se ha introducido el motivo del rechazo',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','msjFaltaMotivoRechazo'         ,'2','Non se introduciu o motivo de rexeitamento',null,null);

GO

--Tareas #345275 [CONS] -SIR- Crear pantalla de presentación de los datos del intercambio
-- Alejandro Díaz

-- Textos de las pantallas del SIR para visualizar el XML
--TITULOS ----------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titVerDatosIntercambio'        ,'1','Datos del intercambio',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','titVerDatosIntercambio'        ,'2','Datos do intercambio',null,null);
--BOTONES ----------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','gbVerDatosIntercambio'         ,'1','Ver intercambio',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','gbVerDatosIntercambio'         ,'2','Ver intercambio',null,null);

-- Textos de la pantalla altaRE
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','gbEnviarAsiento'               ,'1','Enviar asiento',null,null);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','gbEnviarAsiento'               ,'2','Enviar asiento',null,null);

GO

-----------------------------------------------------------------------
--------------------- FIN PARCHE 18.00.00 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.01.00 --------------------------
-----------------------------------------------------------------------

-- Tareas #340461: Integración Flexia16 - Flexia18
-- Milagros Noya (19/11/2018)
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiq_pestanaOtrosDatos',1,'Otros Datos',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'gEtiqDocuAportados',1,'Documentos aportados',null,null);

-- Tareas #347806 Informar y consultar doble validación de usuario
--Juan García Catoira
	--Textos para la autenticacion en dos pasos
	
	--ETIQUETAS --------------------------------------------------------------------
	INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqAutDosPasos'				,'1','Doble autenticación',null,null);
	INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('1','etiqAutDosPasos'				,'2','Doble autenticación',null,null);
	
	--Adicion de columnas necesarias en A_USU para la autenticacion en dos pasos
	ALTER TABLE A_USU ADD USU_DBL_AUT INT;

GO