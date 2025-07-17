-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.00.00 --------------------------
-----------------------------------------------------------------------

-- Tareas #340541 [CONS] -ENI- Adaptacion y creacion de Script para SQLServer
-- Alejandro Díaz
--El script añade el area PROCEDIMIENTO ENI y el tipo de procedimiento ENI para la creacion del procedimiento para la importacion de expedientes ENI.
-- Declaracion de variables
VAR codigoTipoPro NUMBER;
VAR codigoArea NUMBER;

-- Inicializacion de variables
BEGIN
  SELECT max(TPR_COD) + 1 INTO :codigoTipoPro FROM A_TPR;
  SELECT max(ARE_COD) + 1 INTO :codigoArea FROM A_ARE;
END;
/
-- AREAS -----------------------------------------------------------------------
INSERT INTO A_ARE (ARE_COD) VALUES (:codigoArea);
INSERT INTO A_AML (AML_COD,AML_CMP,AML_LENG,AML_VALOR) VALUES (:codigoArea,'NOM','1','PROCEDIMIENTO ENI');
-- TIPOS PROCEDIMIENTO ---------------------------------------------------------
INSERT INTO A_TPR (TPR_COD) VALUES (:codigoTipoPro);
INSERT INTO A_TPML (TPML_COD, TPML_CMP, TPML_LENG, TPML_VALOR) VALUES (:codigoTipoPro,'NOM','1','ENI');
-- Traducciones ----------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'tituloImportarExp', 1, 'Importar expediente');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'tituloImportarExp', 2, 'Importar expediente');
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'etiqBotonImportar', 1, 'Expediente a importar*');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'etiqBotonImportar', 2, 'Expediente a importar*');
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'msjTipoEntradaImport', 1, '* El fichero de entrada debe ser un fichero comprimido en formato ZIP con la siguiente estructura:');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'msjTipoEntradaImport', 2, '* O ficheiro de entrada debe ser un ficheiro comprimido en formato ZIP coa seguinte estrutura:');
--------------------------------------------------------------------------------
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'txtIndiceImportar', 1, '<b>INDICE.XML</b>(contenido del Ã­ndice electrÃ³nico)');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'txtIndiceImportar', 2, '<b>INDICE.XML</b>(contido do Ã­ndice electrÃ³nico)');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'txtDocumentoImportar', 1, '<b>DOC_<idDocumento>.xml </b>(un xml por documento, idDocumento es el identificador contenido dentro de los metadatos ENI en el xml. Ejemplo:<br/>DOC_ES_E00010207_2010_MPR00000000000000000000000000010201.xml)');
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES) VALUES (4, 'txtDocumentoImportar', 2, '<b>DOC_<idDocumento>.xml </b>(un xml por documento, idDocumento Ã© o identificador contido dentro dos metadatos ENI no xml. Exemplo:<br/>DOC_ES_E00010207_2010_MPR00000000000000000000000000010201.xml)');
--------------------------------------------------------------------------------



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
COMMIT;

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

COMMIT;

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
commit;

-- Tareas #347806 Informar y consultar doble validación de usuario
--Juan García Catoira
	--Textos para la autenticacion en dos pasos
	
	--ETIQUETAS --------------------------------------------------------------------
	INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('5','etiqAutDosPasos'				,'1','Doble autenticación',null,null);
        INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('5','etiqAutDosPasos'				,'4','autentifikazio bikoitza',null,null);
        INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('9','etiqAutDosPasos'				,'1','Doble autenticación',null,null);
        INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES ('9','etiqAutDosPasos'				,'4','autentifikazio bikoitza',null,null);
	
	--Adicion de columnas necesarias en A_USU para la autenticacion en dos pasos
	
	ALTER TABLE A_USU ADD (
		USU_DBL_AUT NUMBER DEFAULT 0 NOT NULL ENABLE
	)
COMMIT;


-- Tareas #430701 [LANBIDE] Parametrización del proceso de carga masiva de registros: migración de solicitudes del procedimiento ACASE
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(1, 'codDescProcVacio', 1, 'Código o descripción de procedimiento está vacio', NULL, NULL);
COMMIT;

-- Tareas #432828 [LANBIDE] Portafirmas - Buzón: Datos de Usuario
ALTER TABLE A_USU ADD USU_BUZFIR Varchar2(50) DEFAULT NULL;
COMMENT ON COLUMN A_USU.USU_BUZFIR IS 'Buzón de Firma del usuario';

INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(5, 'etiq_buzonFirma', 1, 'Buzón Firma', NULL, NULL);
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(9, 'etiq_buzonFirma', 1, 'Buzón Firma', NULL, NULL);
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(5, 'msgValBuzonFirma', 1, 'Si el usuario posee capacidad de firma debe tener un buzón de firma.', NULL, NULL);
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(9, 'msgValBuzonFirma', 1, 'Si el usuario posee capacidad de firma debe tener un buzón de firma.', NULL, NULL);

INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(5, 'etiq_buzonFirma', 4, 'Sinadura Postontzia', NULL, NULL);
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(9, 'etiq_buzonFirma', 4, 'Sinadura Postontzia', NULL, NULL);
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(5, 'msgValBuzonFirma', 4, 'Erabiltzaileak sinatzeko gaitasuna badu, sinadura kutxa bat eduki behar du.', NULL, NULL);
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(9, 'msgValBuzonFirma', 4, 'Erabiltzaileak sinatzeko gaitasuna badu, sinadura kutxa bat eduki behar du.', NULL, NULL);

-- Tareas #433554 [LANBIDE] Portafirmas - Envío de documentos de tramitación al Portafirmas
-- Se inserta titulo de la cabecera de la tabla de seleccionar firmante
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(4, 'gEtiq_BuzFir', 1, 'Buzón Firma', NULL, NULL);
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(4, 'gEtiq_BuzFir', 4, 'Sinadura Postontzia', NULL, NULL);


COMMIT;



-- Tareas #460730 [LANBIDE] Portafirmas - Modificar funcionalidad para no permitir eliminar documentos rechazados
-- Se inserta texto para informar al usuario que no permite eliminar documento rechazado
INSERT INTO A_TEX ( TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO,TEX_FEC ) VALUES ( 4, 'msjNoElimDocRechazado', 1, 'No se puede eliminar un documento rechazado.', NULL, NULL);
INSERT INTO A_TEX ( TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO,TEX_FEC ) VALUES ( 4, 'msjNoElimDocRechazado', 4, 'Ezin da baztertutako dokumentua ezabatu.', NULL, NULL);
COMMIT;

-- #462103	Registro - [14880] -Retramitación de documentos por cambio de procedimiento
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioProTitulo','1','Retramitacion de Documentos por cambio de procedimiento',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioProTitulo','4','Retramitacion de Documentos por cambio de procedimiento_EU',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioProSelOwner','1','Seleccione el procedimiento Autorizado Retramitar(Owner): ',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioProSelOwner','4','Seleccione el procedimiento Autorizado Retramitar(Owner)_EU',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioCampoOblig','1','Campo Obligatorio',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioCampoOblig','4','Campo Obligatorio_EU',null,null);

commit;

-- #462103 Registro - [14880] - Retramitación de documentos por cambio de procedimiento  -  Insert Etiquetas 
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msjErrorModifRegRetramitar','1','Anotacion de Registro Modificada. Pero No se pudo retramitar la documentacion. Puede que no visualicen los documentos correctamente, Consulte el equipo de soporte.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msjErrorModifRegRetramitar','4','Anotacion de Registro Modificada. Pero No se pudo retramitar la documentacion. Puede que no visualicen los documentos correctamente, Consulte el equipo de soporte.EU',null,null);
COMMIT;

-- #467452 Tareas #462103: Registro - [15905] - Retramitación de documentos por cambio de procedimiento : Registro - [15241] - Retramitación de documentos por cambio de procedimiento - Agregar opción registrar documentos en contexto LANRE
update a_tex set tex_des='Registro/Retramitacion de Documentos por cambio de procedimiento' where tex_cod='msgRetramiDocCambioProTitulo' and tex_idi=1;
update a_tex set tex_des='Registro/Retramitacion de Documentos por cambio de procedimiento_EU' where tex_cod='msgRetramiDocCambioProTitulo' and tex_idi=4;
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRetramitar','1','Retramitar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRetramitar','4','Retramitrar_EU',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRegistrar','1','Registrar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRegistrar','4','Registrar_EU',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRegisInfo','1','Para registrar documentos que han fallado en el proceso de digitalizacion, debes Tener la entrada en estado Pendiente Fin de Digitalizacion (TablA R_RES: FIN_DIGITALIZACION=0) y los documentos ser Compulsados (Tabla R_RED: RED_DOCDIGIT=1).',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRegisInfo','4','Para registrar documentos que han fallado en el proceso de digitalizacion, debes Tener la entrada en estado Pendiente Fin de Digitalizacion (TablA R_RES: FIN_DIGITALIZACION=0) y los documentos ser Compulsados (Tabla R_RED: RED_DOCDIGIT=1)._EU',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRegisOID','1','Indique el OID a registrar',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRegisOID','4','Indique el OID a registrar_EU',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRegisDatUsu','1','Datos Usuario Realiza Operacion:',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocCambioRegisDatUsu','4','Datos Usuario Realiza Operacion_EU:',null,null);
COMMIT;

-- Tareas #489979 FLEXIA [LANBIDE] [CONS] - Error al generar la Modificacion de un interesado cambiando los datos personales
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntNomA',1,'Nombre antiguo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntApel1A',1,'Primer apellido antiguo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntApel2A',1,'Segundo apellido antiguo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntTipoDocA',1,'Tipo documento antiguo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntDocA',1,'Documento antiguo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntTelA',1,'Teléfono antiguo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntEmailA',1,'Email antiguo',NULL,NULL);

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntNomN',1,'Nombre nuevo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntApel1N',1,'Primer apellido nuevo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntApel2N',1,'Segundo apellido nuevo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntTipoDocN',1,'Tipo documento nuevo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntDocN',1,'Documento nuevo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntTelN',1,'Teléfono nuevo',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpIntEmailN',1,'Email nuevo',NULL,NULL);

commit;

-- Tareas #496102 [LANBIDE] [CONS] Buzón entradas - Filtro de registros con documentos pendientes de catalogar
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(4, 'etiqRegPendCatalogacion', 1, 'Reg. pend. catalogar', NULL, NULL);
INSERT INTO A_TEX (TEX_APL, TEX_COD, TEX_IDI, TEX_DES, TEX_USO, TEX_FEC) VALUES(4, 'etiqRegPendCatalogacion', 4, 'Reg. pend. katalogoa', NULL, NULL);
commit;

-- Tareas #501437 FLEXIA-PRO. [LANBIDE] [CONS] Portafirmas - [18164] - Registrar motivo de rechazo de firma en REGEXLAN
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiq_observacionFirma',1,'Observaciones',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'etiq_observacionFirma',4,'Behaketak',NULL,NULL);
commit;


-- Tareas #530501: [LANBIDE] [CONS] Nuevo movimiento en el histórico de movimientos de una anotación para registrar el cambio del valor de "registroTelematico"
-- Mila Noya
--23/11/2021
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'histAnotTel',1,'Anotación telemática',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (1,'histAnotTel',4,'Anotación telemática',null,null);
COMMIT;

-- Tareas #533325: [LANBIDE] [CONS] Histórico operaciones expediente - Incorporar nueva operación Anulación de Deuda No RGI
-- Mila Noya
--03/12/2021
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAnularDeuda',1,'Anulación de deuda ',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4,'eMovExpAnularDeuda',4,'Anulación de deuda ',null,null);
COMMIT;

-- Tareas #559715: [LANBIDE] [CONS] - Incidencias en la actualización del estado de firma del documento, en el envío a portafirmas
-- Mila Noya
--31/03/2022
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msjProcesoLento',1,'El proceso puede tardar. Por favor, espere a que finalice sin actualizar o abandonar la página.',NULL,NULL);
INSERT INTO A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) VALUES (4,'msjProcesoLento',4,'El proceso puede tardar. Por favor, espere a que finalice sin actualizar o abandonar la página.',NULL,NULL);
COMMIT;


-- Tareas #580255: [LANBIDE] [CONS] Permisos usuario - Nueva directiva "Mantenimiento pestaña otros documentos"
-- Mila Noya
--30/06/2022

--Insertamos directiva
Insert into A_DIRECTIVA (CODIGO,APL,ETIQ) values ('NO_TRAMITAR_SOLO_MANT_OTROSDOC','4','EtiqDirectivaMantOtrosDoc');

--Insertamos etiqueta de directiva
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (5,'EtiqDirectivaMantOtrosDoc',1,'Sólo se pueden gestionar pestaña ''Otros documentos''',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (9,'EtiqDirectivaMantOtrosDoc',1,'Sólo se pueden gestionar pestaña ''Otros documentos''',null,null);

COMMIT;

-- Tareas #594137: [LANBIDE] [CONS] Usuarios con login duplicado
-- Mila Noya
-- 20/09/2022

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('5','msgErrorGenerico','1','Se ha producido un error. Consulta con el administrador',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('5','msgErrorGenerico','2','Produciuse un erro. Contacte co administrador',null,null);

-- Tarea #598918: [LANBIDE] [CONS] Adaptar interfaz de mantenimiento de desplegables externos.
--05/10/2022
--Milagros Noya
--Se anhaden etiquetas de internacionalizacion
--INI
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('9','etiqValorId2TablaCampoExterno','1','Campo valor idioma alternativo:',null,null);
/*Gallego*/
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('9','etiqValorId2TablaCampoExterno','2','Campo valor idioma alternativo:',null,null);
/*Euskera*/
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('9','etiqValorId2TablaCampoExterno','4','Campo valor idioma alternativo:',null,null);
--FIN

-- Tarea #599358: SIR - REGEXLAN - [23688] - Creación de registros de salida SIR - Gestion SVN Desarrollos
-- 26/12/2022
-- Milagros Noya
-- Se anhaden etiquetas para multilenguaje
-- INI
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','salidaSIR','1','SALIDA SIR',null,null);
--Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','salidaSIR','4','ESI IRTEERA',null,null);
commit;

Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgSelecDestinoSalidaSIR','1','Seleccionar Destino Salida SIR',null,null);
--Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgSelecDestinoSalidaSIR','4','Hautatu ESI Irteerako Helmuga',null,null);
commit;

Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgSelecOrigenSalidaSIR','1','Falta dato Unidad de Origen',null,null);
--Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgSelecOrigenSalidaSIR','4','Jatorrizko unitatearen datua falta da',null,null);
commit;

Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgAltaSalidaSIRCorrecto','1','Se ha dado de alta correctamente el registro de salida {0} en el SIR',null,null);
--Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgAltaSalidaSIRCorrecto','4','Behar bezala alta eman zaio {0} irteera-erregistroari ESIan',null,null);
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgErrorAltaSalidaSIR','1','Se ha presentado un error al procesar la peticion de alta de salida en SIR. {0}',null,null);
--Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgErrorAltaSalidaSIR','4','Errorea gertatu da irteera-altaren eskaera ESIn prozesatzean. {0} ',null,null);
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgErrorAltaSalidaSIRNoProc','1','No se ha recibido el codigo de procedimiento, no se puede crear la anotacion de salida registro en SIR',null,null);
--Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgErrorAltaSalidaSIRNoProc','4','Ez da jaso prozeduraren kodea, ezin da sortu erregistro-irteeraren oharra ESIn',null,null);
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgErrorAltaSalidaSIRNoResp','1','No se ha recibido respuesta de WS externo, no se puede crear la anotacion de salida registro en SIR',null,null);
--Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgErrorAltaSalidaSIRNoResp','4','Ez da kanpoko WSen erantzunik jaso; ezin da ESIn erregistro-irteeraren oharra sortu',null,null);
commit;
-- FIN

-- Tarea #599358: SIR - REGEXLAN - [23688] - Creación de registros de salida SIR - Gestion SVN Desarrollos
-- 11/01/2023
-- Milagros Noya
-- Se anhaden etiquetas para multilenguaje
-- INI
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgSalidaNoDadaAltaSIR','1','Registro de Salida no dado de alta en el SIR. Verifique la unidad de Destino. Pulse Guardar o Modificar y luego Guardar para intentar de nuevo el Alta en el SIR',null,null);
--Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgSalidaNoDadaAltaSIR','4','ESIan alta eman gabeko irteera-erregistroa. Egiaztatu xede-unitatea. Sakatu Gorde edo Aldatu eta gero Gorde, berriro ESIn alta ematen saiatzeko.',null,null);
commit;
-- FIN

-----------------------------------------------------------------------
--------------------- FIN PARCHE 18.01.00 --------------------------
-----------------------------------------------------------------------
---------------------.................----------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.03.00 --------------------------
-----------------------------------------------------------------------

-- Tarea #650760: [LANBIDE] [MRG] - Traduccion Literales - Generales y Adaptaciones Retramitacion documentos por cambio Procedimiento
-- 10/04/2023
-- Milagros Noya
-- Se anhaden etiquetas para multilenguaje
-- INI
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocRegistrarTodos','1','Registrar en Dokusi Todos los Documentos de la Entrada',null,null);
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocRegistrarDocEspec','1','Registrar en Dokusi un Documento Especifico de la Entrada',null,null);
update a_tex  set tex_des='Retramitar Doc.' where tex_cod='msgRetramiDocCambioRetramitar' and tex_idi=1;

-- Retramitado Correctamente.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocOK','1','Retramitado Correctamente.',null,null);
-- No Retramitado.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNOOK','1','No Retramitado.',null,null);
-- OID no recuperado.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNoOID','1','OID no recuperado.',null,null);
-- No se han recuperado los documentos  asociados a la entrada.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNoDocs','1','No se han recuperado los documentos  asociados a la entrada.',null,null);
-- Error al procesar la operacion.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocERROROpe','1','Error al procesar la operacion.',null,null);
-- Registrado  Correctamente.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocRegOK','1','Registrado  Correctamente.',null,null);
-- No Registrado.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocRegNOOK','1','No Registrado.',null,null);
-- No es un documento Compulsado.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNoComp','1','No es un documento Compulsado.',null,null);
-- Datos del Documento no recuperados de BD para ese OID.
Insert into a_tex (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('1','msgRetramiDocNODataBD','1','Datos del Documento no recuperados de BD para ese OID.',null,null);

commit;
-- FIN

-----------------------------------------------------------------------
--------------------- FIN PARCHE 18.03.00 --------------------------
-----------------------------------------------------------------------
---------------------.................----------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.04.04 --------------------------
-----------------------------------------------------------------------

-- Tarea #714678: [LANBIDE] Nuevo movimiento borrado campos suplementarios de trámite
-- 14/11/2023
-- Milagros Noya
-- Se anhaden etiquetas para multilenguaje del nuevo movimiento
-- INI

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('4','eMovExpBorrarCamposTram','1','Borrado de campos suplementarios de trámite',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('4','eMovExpBorrarCamposTram','4','Borrado de campos suplementarios de trámite',null,null);
commit;

-- FIN

-- Tarea #721095: [LANBIDE] Un usuario cualquiera puede iniciar un trámite manual cuyo inicio y gestión está limitado a un determinado cargo.
-- 11/12/2023
-- Milagros Noya
-- Se anhaden dos etiquetas para multilenguaje tanto para el caso en el que el usuario no tenga permiso para iniciar el trámite como para el caso en el que si.
-- INI
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('4','msgSinPermisoInicioTramite','1','El usuario no tiene permiso para iniciar el trámite.',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('4','msgSinPermisoInicioTramite','2','O usuario non ten permiso para iniciar o trámite.',null,null);

COMMIT;
-- FIN

-- Tarea #718430: Plantillas REGEXLAN - [31619] - Añadir filtro de plantillas activas
-- 05/02/2024
-- Pablo Bugia
-- Se anhade una etiqueta para el desplegable de Plantillas activas en la pantalla de Documentos.
-- INI
Insert into A_TEX(TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqAct',1,'Plantillas activas',NULL,NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (6,'etiqAct',4,'Txantiloi aktiboak',NULL,NULL);

COMMIT;
--FIN

-- Tarea #692194: Roles expediente - REGEXLAN - [30018] - Validación de roles
-- 15/02/2024
-- Jaime Lacabex
-- Se anhaden etiquetas para multilenguaje tanto en castellano como en euskera
-- INI

---Euskera
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4, 'msgErrorUnoValRolGrabarExp', 4, 'Ezin da espedientea grabatu, rol-kopurua eta hirugarrenena desberdinak direlako.', NULL, NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4, 'msgErrorDosValRolGrabarExp', 4, 'Ezin da espedientea grabatu, rolak errepikatzen dira.', NULL, NULL);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4, 'msgErrorTresValRolGrabarExp', 4, 'Ezin da espedientea grabatu, dokumentuak errepikatuta daude.', NULL, NULL);
--Castellano
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4, 'msgErrorUnoValRolGrabarExp', 1, 'No se puede grabar el expediente, debido a que el número de roles es distinto al de terceros.', null, null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4, 'msgErrorDosValRolGrabarExp', 1, 'No se puede grabar el expediente, se repiten los roles.', null, null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values (4, 'msgErrorTresValRolGrabarExp', 1, 'No se puede grabar el expediente, los documentos están repetidos.', null, null);

COMMIT;

-- FIN

-- Tarea #791173: CAS - REGEXLAN - [36172] - Contador con número de días que faltan para que caduque la contraseña
-- 06/03/2025
-- Milagros Noya
-- Se anhaden etiquetas para multilenguaje tanto en castellano como en euskera
-- INI

---Euskera

Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('5','etiqNombreUsuario','4','Erabiltzailea',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('5','etiqCambiarContrasena','4','Pasahitza aldatu',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('5','etiqDiasParaCaducidad','4','Zure pasahitza iraungitzeko {0} egun gelditzen dira.',null,null);
--Castellano
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('5','etiqNombreUsuario','1','Usuario/a',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('5','etiqCambiarContrasena','1','Cambiar contraseña',null,null);
Insert into A_TEX (TEX_APL,TEX_COD,TEX_IDI,TEX_DES,TEX_USO,TEX_FEC) values ('5','etiqDiasParaCaducidad','1','Quedan {0} días para que su contraseña caduque.',null,null);

COMMIT;

-- FIN