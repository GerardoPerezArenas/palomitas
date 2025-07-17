package es.altia.agora.technical;
import java.io.File;

public interface ConstantesDatos { 


	//	 Constantes para aplicaciones.
    public static final int APP_REGISTRO_ENTRADA_SALIDA = 1;
    public static final int APP_PADRON = 2;
    public static final int APP_TERCEROS_TERRITORIO = 3;
    public static final int APP_GESTION_EXPEDIENTES = 4;
    public static final int APP_ADMINISTRADOR_GENERAL = 5;
    public static final int APP_DOCUMENTOS = 6;
    public static final int APP_WEB_CIUDADANO = 7;
    public static final int APP_GENERADOR_INFORMES = 8;
    public static final int APP_ADMINISTRADOR_LOCAL = 9;
    public static final int APP_INFORMES_DIRECCION = 10;
    public static final int APP_PORTAFIRMAS = 11;
    public static final int APP_PREFERENCIAS_USUARIO = 12;
    public static final int APP_INFORMACION_TRIBUTARIA = 13;
    public static final int APP_REGISTRO_PLANEAMIENTO = 14;
    public static final int APP_GESTION_FORMULARIOS = 15;
    public static final int APP_USUARIOS_FORMULARIOS = 18;
    public static final int APP_PARAMETROS_USUARIOS = 19;
    public static final int APP_GESTION_INFORMES = 16;
    public static final int APP_INTEGRACION_SW = 17;

    // Constantes para obligatoriedad de Servicios Web.
    public static final int WS_TRAMITACION_OPCIONAL = 0;
    public static final int WS_TRAMITACION_OBLIGATORIO = 1;

    // Constantes para idiomas.
    public static final int IDIOMA_CASTELLANO = 1;
    public static final int IDIOMA_GALLEGO = 2;
    public static final int IDIOMA_CATALÁN = 3;
    public static final int IDIOMA_EUSKERA = 4;
    public static final int IDIOMA_VALENCIANO = 5;

    // Constantes para locales.
    public static final String CLAVE_LOCALE_CASTELLANO = "es";
    public static final String LOCALE_CASTELLANO = "es_ES";
    public static final String LOCALE_GALLEGO = "gl_ES";
    public static final String LOCALE_CATALÁN = "ca_ES";
    public static final String LOCALE_EUSKERA = "eu_ES";
    public static final String LOCALE_VALENCIANO = "va_ES";
    
    // Constantes para las busquedas de anotaciones de registro dependiendo del estado de la misma.
    public static final int REG_ANOTACION_BUSCAR_TODAS = -10;
    public static final int REG_ANOTACION_BUSCAR_NO_ANULADAS = -9;
    
    // Constantes para los estados de las anotaciones de registro.
    public static final int REG_ANOTACION_ESTADO_PENDIENTE = 0;
    public static final int REG_ANOTACION_ESTADO_ACEPTADA = 1;
    public static final int REG_ANOTACION_ESTADO_RECHAZADA = 2;
    public static final int REG_ANOTACION_ESTADO_ANULADA = 9;
    public static final int REG_ANOTACION_ESTADO_ACEPTADA_EN_DESTINO = 3;

    // Constantes para los tipos de entradas de registro dependiendo de origen y destino.
    public static final int REG_ANOT_TIPO_ORDINARIA = 0;
    public static final int REG_ANOT_TIPO_DESTINO_OTRO_REG = 1;
    public static final int REG_ANOT_TIPO_ORIGEN_OTRO_REG = 2;

    // Codigo de departamento por defecto
    public static final int REG_COD_DEP_DEFECTO = 1;
    
    // Tipos de movimientos de histórico de movimientos (para anotaciones)
    public static final int HIST_ANOT_ALTA = 0;
    public static final int HIST_ANOT_MODIFICAR = 1;
    public static final int HIST_ANOT_RESERVA = 2;
    public static final int HIST_ANOT_ANULAR = 3;
    public static final int HIST_ANOT_DESANULAR = 4;
    public static final int HIST_ANOT_ACEPTAR = 5;
    public static final int HIST_ANOT_RECHAZAR = 6;
    public static final int HIST_ANOT_INICIAR = 7;
    public static final int HIST_ANOT_ADJUNTAR = 8;
    public static final int HIST_ANOT_RECUPERAR = 9;
    public static final int HIST_ANOT_ANULAR_RES = 10;
    public static final int HIST_ANOT_IMPORTAR = 11;
    public static final int HIST_ANOT_ELIMINAR_RECHAZO = 12;
    public static final int HIST_ANOT_ENVIO_CORREO = 13;
    public static final int HISTORICO_ANOTACION_CANCELAR_INICIO_EXPEDIENTE = 14;
    public static final int HISTORICO_ANOTACION_CANCELAR_ADJUNTAR_EXPEDIENTE = 15;
    public static final int HIST_ANOT_ACEPTAR_EN_DESTINO = 16;
	public static final int HIST_DOC_DIGIT = 17;
	public static final int HIST_ANOT_FINALIZAR = 18;
	public static final int HIST_ANOT_FINALIZAR_MODIFICACION = 19;
    
    // Tipos de razones de envio de correo del hist?rico de movimientos	
    public static final int HIST_ENVIO_CORREO_DOC_COTEJADO = 0;
    // Tipos de entidades del histórico de movimientos
    public static final String HIST_ENTIDAD_ANOTACION = "ANOTACION";
	public static final String HIST_REGISTRO_DOCUMENTO = "DOCUMENTO";
    
    // Codigos del tercero y uor ficticios para anulacion de reservas
    public static final int COD_TER_ANULAR_RES = -99;
    public static final int VER_TER_ANULAR_RES = 1;
    public static final int DOM_TER_ANULAR_RES = -99;
    public static final int ROL_TER_ANULAR_RES = -99;
    public static final String UOR_ANULAR_RES = "-99";
    
    // Codigos para unidad tramitadora de un tramite
    public static final String TRA_UTR_OTRAS = "0";      // Otras
    public static final String TRA_UTR_CUALQUIERA = "1"; // Cualquiera
    public static final String TRA_UTR_ANTERIOR = "2";   // La del trámite anterior
    public static final String TRA_UTR_INICIA = "3";     // La que lo inicia
    public static final String TRA_UTR_EXPEDIENTE = "4"; // La del expediente

    // Constantes con los nombres de los parámetros para multi-idioma
    public static final String LISTA_IDIOMAS        = "listaIdiomas";
    public static final String DESCRIPCION_IDIOMA = "descripcionIdioma";
    
    // Nombre del parámetro del que se recupera el nombre de la licencia de la instalación
    public static final String NAME_LICENSE   = "name_license";
    // Atributo de la request que contiene el número máximo de usuarios para la instalación según la licencia
    public static final String NUM_MAX_USERS  = "NUM_MAX_USERS";
    // Atributo del ámbito de la aplicación en el que se almacena la licencia de la aplicación
    public static final String APPLICATION_LICENSE  = "APPLICATION_LICENSE";
    public static final String MODULOS_APLICACION   = "MODULOS_APLICACION";  
    // Constante de sesión que contiene el número total de usuarios en la BD
    public static final String NUM_TOTAL_USUARIOS   = "NUM_TOTAL_USUARIOS";  
    
    public static final String FECHA_NUNCA_LICENCIA_UPPER = "NUNCA";
    public static final String FECHA_NUNCA_LICENCIA_LOWER = "nunca";
    public static final String NUNCA_MAX_USUARIO_LICENCIA = "NUNCA";
    
    // Atributos de la request que indican si un documento subido al servidor excede el tamaño máximo permitida
    // y si la extensión del fichero subido es válida
    public static final String TAM_MAX_FILE_EXCEED        = "TAM_MAX_FILE_EXCEED";
    public static final String EXTENSION_FILE_INCORRECT   = "EXTENSION_FILE_INCORRECT";
    public static final String TAM_MAX_FILE_BYTE          = "TAM_MAX_FILE_BYTE";
    public static final String DOCUMENT_TITLE_REPEATED    = "DOCUMENT_TITLE_REPEATED";
    
    public static final String EXTENSION_PERMITED         = "EXTENSION_PERMITED";
    public static final String DESCRIPCION_BYTES          = "bytes";
    public static final String DESCRIPCION_MEGABYTES      = "Mb";
    public static final String DOCUMENTO                  = "DOCUMENTO";
    
    public static final String ERROR_FILESIZE_UPLOAD      = "ERROR_FILESIZE_UPLOAD";    
    public static final int DIVISOR_BYTES                 = 1000000;    
    
    public static final String TAM_MAX_DOC_REGISTRO       = "TAM_MAX_DOC_REGISTRO";
    public static final String TAM_MAX_DOC_EXPEDIENTE     = "TAM_MAX_DOC_EXPEDIENTE";
    public static final String EXTENSION_DOC_REGISTRO     = "EXTENSION_DOC_REGISTRO";
    public static final String EXTENSION_DOC_EXPEDIENTE   = "EXTENSION_DOC_EXPEDIENTE";
    public static final String TIPOS_MIME_EXPEDIENTE      = "TIPOS_MIME_EXPEDIENTE";
    public static final String TIPOS_MIME_REGISTRO        = "TIPOS_MIME_REGISTRO";
    public static final String TIPO_MIME_DOC_TRAMITES     = "application/msword";
    
    // Parámetro de sesión que almacena el tamaño total de todos los documentos subidos al servidor para el Registro
    public static final String TAM_MAX_ALL_FILE_REGISTRO  = "TAM_MAX_ALL_FILE_EXCEED_REGISTRO";       
    // Parámetro de sesión que almacena el tamaño total de todos los documentos subidos al servidor para en la Gestión
    // de expedientes
    public static final String TAM_MAX_ALL_FILE_EXPEDIENTES = "TAM_MAX_ALL_FILE_EXCEED_EXPEDIENTES";
    
    // Tamáño máximo para
    public static final String FILESIZE_MAX_ALL_REGISTRO    = "FILESIZE_MAX_ALL_REGISTRO";
    public static final String FILESIZE_MAX_ALL_EXPEDIENTES = "FILESIZE_MAX_ALL_EXPEDIENTES";
    
    public static final String LIMITE_GLOBAL_DOCUMENTOS_REGISTRO    = "LIMITE_GLOBAL_DOCUMENTOS_REGISTRO";
    public static final String LIMITE_GLOBAL_DOCUMENTOS_EXPEDIENTES = "LIMITE_GLOBAL_DOCUMENTOS_EXPEDIENTES";
    
    public static final String EXCEED_LIMITE_GLOBAL_DOCUMENTOS_EXPEDIENTES = "EXCEED_LIMITE_GLOBAL_DOCUMENTOS_EXPEDIENTES";
    public static final String EXCEED_LIMITE_GLOBAL_DOCUMENTOS_REGISTRO    = "EXCEED_LIMITE_GLOBAL_DOCUMENTOS_REGISTRO";
    
    // Valores para los parametros propios de cada organizacion
    // Observaciones obligatorias al rechazar anotacion
    public static final String PARAMS_OBS_RECHAZAR_CLAVE = "OBS_OBLIGATORIAS_AL_RECHAZAR";
    public static final String PARAMS_OBS_RECHAZAR_NO = "NO";
    public static final String PARAMS_OBS_RECHAZAR_SI = "SI";

    // Valores para las directivas propias de cada usuario
    // El usuario puede dar altas de salidas desde tramitacion
    public static final String DIRECTIVA_SALIDAS_DESDE_TRAMITAR = "SALIDAS_DESDE_TRAMITAR";
    
    // Directiva de usuario para no tramitar desde el modo consulta
    public static final String NO_TRAMITAR_MODOCONSULTA = "NO_TRAMITAR_MODOCONSULTA";
    
    public static final String REGISTRO_S_SOLO_UORS_USUARIO = "REGISTRO_S_SOLO_UORS_USUARIO";

    //Directiva de usuario que no permite tramitar desde ningún menú.
    public static final String NO_TRAMITAR = "NO_TRAMITAR";
    
    //Directiva de usuario que no permite tramitar desde ningún menu, exceptuando el mantenimiento de la  pestana 'Otros documentos'
    public static final String NO_TRAMITAR_SOLO_MANT_OTROSDOC = "NO_TRAMITAR_SOLO_MANT_OTROSDOC";

    //Directiva de usuario que no permita relacionar expedientes a otro.
    public static final String NO_RELACIONAR_EXP = "NO_RELACIONAR_EXP";

    // Nombre de entidades utilizadas en el alta de un documento
    public static final String ENTIDAD_DOCUMENTO_EXPEDIENTE_INTERESADO = "EXPEDIENTEINTERESADO";
    public static final String ENTIDAD_DOCUMENTO_RELACION                      = "RELACION";
    public static final String ENTIDAD_DOCUMENTO_REGISTRO                      = "REGISTRO";
    public static final String NODO_INTERESADO                                        = "INTERESADO";
    public static final String NOMBRE_INTERESADO                                     = "NOMBREINTERESADO";
    public static final String ROL_INTERESADO                                          = "ROLINTERESADO";
    public static final String NOMBRE_COMPLETO                                        = "NOMBRECOMPLETO";
    public static final String APELLIDO1_INTERESADO                                = "APELLIDO1INTERESADO";
    public static final String APELLIDO2_INTERESADO                                = "APELLIDO2INTERESADO";
    public static final String DOCUMENTO_INTERESADO                            = "DOCUMENTO";
    public static final String DOMICILIO_INTERESADO                             = "DOMICILIOINTERESADO";
    public static final String DOMICILIO_NONORMALIZADO                        = "DOMICILIONONORMALIZADO";
    public static final String NORMALIZADO                                          = "NORMALIZADO";
    public static final String TELEFONO_INTERESADO                              = "TELEFONOINTERESADO";
    public static final String CODPOSTAL_INTERESADO                          = "CODPOSTALINTERESADO";
    public static final String PROV_INTERESADO                                     = "PROVINTERESADO";

    public static final String TRAMITE_FINALIZADO_CON_TRAMITES_POSTERIORES       = "tramFinalizadoConTramitesPosteriores";
    public static final String TRAMITE_ORIGEN_CON_TRAMITES_ABIERTOS                   = "tramiteOrigenConTramitesAbiertos";
    public static final String EXPEDIENTE_FINALIZADO_NO_RETROCEDER                     = "expedienteFinalizadoNoRetroceder";
    public static final String TRAMITE_CON_DOCUMENTOS_FIRMADOS                          = "tramiteConDocumentosFirmados";
    public static final String TRAMITE_CON_FORMULARIOS_FIRMADOS                         = "tramiteConFormulariosFirmados";
    public static final String TRAMITE_CON_FORMULARIOS_DOCUMENTOS_FIRMADOS   = "tramiteConFormulariosDocumentosFirmados";
    public static final String TRAMITE_RETROCESO_DE_ESTADO_FINALIZADO                   = "tramiteRetrocesoDeEstadoFinalizado";
    public static final String TRAMITE_REABRIR                  = "tramiteReabrir";

    public static final String ETIQ_FEC_ENTRADA_ANOTACION_REGISTRO                   = "FECENTREGISTRO";
    public static final String ETIQ_NUM_ENTRADA_ANOTACION_REGISTRO                  = "NUMREGISTROENTRADA";
    public static final String FECHA_ENTRADA_REGISTRO_ANOTACION                        = "FECHA_ENTRADA_REGISTRO_ANOTACION";
    public static final String NUMERO_REGISTRO_ANOTACION                                    = "NUMERO_REGISTRO_ANOTACION";
    
    public static final String PREFIJO_PROPIEDAD_ALMACENAMIENTO                         = "Almacenamiento/";
    public static final String SUFIJO_PROPIEDAD_ALMACENAMIENTO                          = "/pluginDocumentosTramitacion";
    public static final String SUFIJO_PROPIEDAD_ALMACENAMIENTO_REGISTRO                 = "/pluginDocumentosRegistro";

   public static final String PROPIEDAD_PLUGIN_ALMACENAMIENTO                           = "Almacenamiento/plugin";
   public static final String SUFIJO_PLUGIN_GESTOR_URL                                         = "/urlGestor";
   public static final String SUFIJO_PLUGIN_GESTOR_USUARIO_GESTOR                    = "/usuarioGestor";
   public static final String SUFIJO_PLUGIN_GESTOR_PASSWORD_GESTOR                 = "/passwordGestor";
   public static final String SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ                        = "/carpetaRaiz";
   public static final String SUFIJO_PLUGIN_NOMBRE_GESTOR                                  = "/nombreGestor";
   public static final String SUFIJO_PLUGIN_IMPL_CLASS                                          = "/implClass";
   public static final String GESTOR                                                                        = "GESTOR";

	public static final String BARRA															= "/";
	public static final String BARRA_INVERTIDA													= "\\";
   public static final String EXTENSION_DOCUMENTOS_TRAMITACION_WORD            = "doc";
   public static final String EXTENSION_DOCUMENTOS_TRAMITACION_WORD_CON_PUNTO        = ".doc";
   public static final String EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE   = "odt";
   public static final String EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE_CON_PUNTO  = ".odt";
   public static final String EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION= "pk7";
   public static final String EXTENSION_DOCUMENTOS_TRAMITACION_OFFICE = "docx";
   public static final String EXTENSION_DOCUMENTOS_TRAMITACION_OFFICE_CON_PUNTO = ".docx";
   
    public static final String EXTENSION_FICHERO_PDF = "pdf";
	public static final String EXTENSION_FICHERO_XML = "xml";
	public static final String EXTENSION_FICHERO_XML_CON_PUNTO = ".xml";


   public static final String PROPIEDAD_LONGITUD_CODIGO_TRAMITE_VISIBLE_BD            ="longitud_codigo_tramite_visible";
   public static final String PROPIEDAD_LONGITUD_NUMERO_DOCUMENTO_BD                   ="longitud_numero_documento";
   public static final String PROPIEDAD_LONGITUD_OCURRENCIA_TRAMITE_INTERNO_BD   ="longitud_ocurrencia_tramite";

   public static final String CERO = "0";
   public static final String DOT = ".";
   
   public static final String PROP_ALMACEN_GESTOR_CABECERA_HTML_RELACION          = "Almacenamiento/GESTOR/cabecera/ficheroHTMLDocumentosRelacion";
   public static final String PROP_ALMACEN_GESTOR_CUERPO_HTML_RELACION              = "Almacenamiento/GESTOR/cuerpo/ficheroHTMLDocumentosRelacion";
   public static final String PROP_ALMACEN_GESTOR_PIE_HTML_RELACION                     = "Almacenamiento/GESTOR/pie/ficheroHTMLDocumentosRelacion";

   public static final String PROP_GESTOR_DOC_EXT_PORTAFIRMAS_CARPETA_RAIZ      = "carpetaRaiz";
   public static final String PROP_GESTOR_DOC_EXT_PORTAFIRMAS_PORTAFIRMAS       = "portafirmas";
   public static final String PROP_GESTOR_DOC_EXT_PORTAFIRMAS_DOCUMENTOS      = "documentos";

   public static final String NOMBRE_FICHERO_LISTADO_DOCUMENTOS_RELACION          = "LISTADO_DOCUMENTOS_RELACION_";
   public static final String TIPO_MIME_HTML                                                                ="text/html";
   public static final String CODIFICACION_UTF_8                                                         ="UTF_8";
   //La constante CODIFICACION_UTF_8 es incorrecta, porque la codificacion no puede ser UTF_8 sino UTF-8. No se elimina 
   //porque se esta utilizando en muchos sitios y se desconoce a que afectaria
   public static final String CODIFICACION_UTF                                     ="UTF-8";
   public static final String CODIFICACION_ISO_8859_1                              ="ISO-8859-1";  
   public static final String TIPO_MIME_DOCUMENTO_WORD                                         ="application/vnd.ms-word";
   public static final String TIPO_MIME_DOCUMENTO_OPENOFFICE                                ="application/vnd.oasis.opendocument.text";
   public static final String TIPO_MIME_DOCUMENTO_PDF                              = "application/pdf";
   public static final String TIPO_MIME_DOCUMENTO_OFFICE                          = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"; 

   public static final String AUTH_ACCESS_MODE                                                        = "Auth/accessMode";
   public static final String AUTH_ACCESS_MODE_DNIE                                               = "dnie";
   public static final String AUTH_ACCESS_MODE_CERT                                               = "cert";
   public static final String AUTH_ACCESS_MODE_DNIE_SERIALNUMBER                       = "SERIALNUMBER=";
   public static final String SIGNO_IGUAL                                                                  = "=";
   public static final int LONGITUD_DNI                                                                     = 9;
   public static final String CONSULTA_SOLO_EXPEDIENTES_TRAMITADOR                   = "CONSULTA_SOLO_EXP_TRAMITADOR";


   public static final String CODIGO_PROVINCIA_DESCONOCIDO = "/codigo_provincia_desconocido";
   public static final String CODIGO_MUNICIPIO_DESCONOCIDO = "/codigo_municipio_desconocido";
   public static final String CODIGO_VIA_DESCONOCIDO            = "/codigo_via_desconocido";
   public static final String ALTA_TERCERO_SIN_DOMICILIO       = "/alta_tercero_sin_domicilio";
   public static final String DESCRIPCION_PROVINCIA_DESCONOCIDA = "/descripcion_provincia_desconocida";
   public static final String DESCRIPCION_MUNICIPIO_DESCONOCIDO = "/descripcion_municipio_desconocida";
   public static final String DESCRIPCION_VIA_DESCONOCIDA            = "/descripcion_via_desconocida";
   public static final String CODIGO_TIPO_VIA_DESCONOCIDA           = "/codigo_tipo_via_desconocida";

   public static final String BLOQUEO_FECHA_HORA_ANOTACION       = "bloqueo_fecha_hora_anotacion";

   public static final String COMMA = ";";
   public static final String DOT_COMMA = ";";

   public static final String ORIGEN_ADJUNTO_DOC_PRESENTADO_BBDD = "BD";
   public static final String GUION                                                         = "-";
   public static final String GUION_BAJO                                                = "_";
   public static final String PREFIJO_DOCUMENTO_RELACION                  = "REL_";

   public static final String BUSQUEDA_TERCERO = "BusquedaTercero";
   public static final String PLUGIN_EGIM            = "EGIM";
   public static final String NOMBRE_ORIGEN      = "NOMBRE_ORIGEN";
   public static final String BUSQUEDA_TERCERO_CONEXION_EGIM_USUARIO      = "conexion/usuario";
   public static final String BUSQUEDA_TERCERO_CONEXION_EGIM_PASSWORD   = "conexion/password";
   public static final String BUSQUEDA_TERCERO_CONEXION_EGIM_MUNICIPIO  = "conexion/municipio";
   public static final String BUSQUEDA_TERCERO_CONEXION_EGIM_ENTIDAD      = "conexion/entidad";


   /** TIPOS DE DOCUMENTO WS DE CONSULTA DE TERCEROS DE E-GIM **/
   public static final String TIPO_DOC_EGIM_SINDOCUMENTO            = "SIN";
   public static final String TIPO_DOC_EGIM_FISICA                          = "FISICA";
   public static final String TIPO_DOC_EGIM_JURIDICA                     = "JURIDICA";
   public static final String TIPO_DOC_EGIM_ADMINISTRACION         = "ADMINISTRACION";
   public static final String TIPO_DOC_EGIM_TARJETA_RESIDENCIA  = "TARJETA_RESIDENCIA";
   public static final String TIPO_DOC_EGIM_PASAPORTE                  = "PASAPORTE";
   public static final String MAPEO_DOCUMENTO_FLEXIA                   = "MAPEO_DOCUMENTO_FLEXIA";
   public static final String MAPEO_DOCUMENTO_EGIM                      = "MAPEO_DOCUMENTO_EGIM";
   public static final String URL_END_POINT                                     = "urlEndPoint";


    //Constantes Gestion Automatica Contraseñas
   public static final String PROPIEDAD_NUMERO_PASSWORD_ROTACION = "password.rotacion.numero";
   public static final String PROPIEDAD_PASSWORD_CADUCIDAD = "password.caducidad";
   public static final String PROPIEDAD_PERIODO_UNIDAD = "password.periodo.unidad";
   public static final String PROPIEDAD_PERIDODO_CANTIDAD = "password.periodo.cantidad";

   public static final String MAPEO_VIA_FLEXIA                        = "MAPEO_VIA_FLEXIA";
   
   public static final String SEPARADOR ="§¥";
   public static final String SEPARADORAJAX ="##"; //En las peticiones ajax los símbolos §¥ se envían mal

    /** CONSTANTES PARA EL PLUGIN DE ALMACENAMIENTO DE DOCUMENTOS EN SHAREPOINT DE POZUELO **/
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_DOMINIO                                                            = "dominio";
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_METADATOS_EXPEDIENTE                                    = "metadatos";



   /***********/
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_URLSERIEDOCUMENTAL               = "urlSerieDocumental";
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_URLSERIEDOCUMENTAL_LISTA     = "urlSerieDocumental/lista";
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_SERIEDOCUMENTAL_LISTA          = "ListaExpedientes";
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_SERIEDOCUMENTAL_URL_ELIMINAR_DOCUMENTO    = "urlSerieDocumental/EliminarDocumento";



  /***************/
   

   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_URLSERIEDOCUMENTAL_LISTA_ALTAEXPEDIENTE   = "urlSerieDocumental/AltaExpediente/lista";
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_CODERROR_OP_ALTAEXPEDIENTE_EXITO              = "codigoError/OperacionAltaExpediente";
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_CODERROR_OP_ALTAEXPEDIENTE_EXISTE             = "codigoError/OperacionAltaExpediente/Existe";
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_CODERROR_OP_ELIMINARDOCUMENTO                 = "codigoError/OperacionEliminarDocumento";


   
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_RESPUESTA_WS_ERRORCODE                                 = "ErrorCode";



   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_SERIE_DOC_METODOS_METADATOS                 = "metodosDocumento/metadatos";
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_SERIE_DOC_METODOS_METADATOS_RELACION = "metodosDocumento/metadatos/relacion";

   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_TIPO_CONTENIDO_EXPEDIENTE       = "createItemCredentials/metadato/TipoContenidoExpediente";
   public static final String ALMACENAMIENTO_DOCUMENTO_GESTOR_NEXTRET_VALOR_CONTENIDO_EXPEDIENTE    = "createItemCredentials/metadato/ValorTipoContenidoExpediente";


   // Prefijo utilizado para las unidades organizativas que hacen de oficina de registro para Lanbide.
   public static final String PREFIJO_OFICINA_REGISTRO_LANBIDE              =  "OF";
   public static final String PREFIJO_MAPEO_ESTADO_ANOTACION_FLEXIA   =  "mapeo_estado_anotacion_flexia";


   public static final String NUM_REGISTROS_PAGINA_CONTROL_ENTRADAS_LANBIDE = "num_registros_pagina_control_entradas_lanbide";

   public static final int CODIGO_TIPO_LISTADO_EXPEDIENTES_PENDIENTES = 2;
   public static final int TIPO_DATO_TEXTO_CORTO_CAMPO_SUPLEMENTARIO  = 2;
   public static final int TIPO_DATO_TEXTO_LARGO_CAMPO_SUPLEMENTARIO  = 4;

   
   public static final String TIPO_DATO_CAMPO_FIJO_NUMEXPEDIENTE    = "E";
   public static final String TIPO_DATO_CAMPO_FIJO_TEXTO            = "T";
   public static final String TIPO_DATO_CAMPO_FIJO_FECHA            = "F";
   public static final String TIPO_DATO_CAMPO_FIJO_DOCUMENTO        = "D";
   public static final String TIPO_DATO_CAMPO_FIJO_NOMBREINTERESADO = "N";

   public static final String TIPO_DATO_CAMPO_SUPL_NUMERICO     = "1";
   public static final String TIPO_DATO_CAMPO_SUPL_TEXTO_CORTO  = "2";
   public static final String TIPO_DATO_CAMPO_SUPL_FECHA        = "3";
   public static final String TIPO_DATO_CAMPO_SUPL_DESPLEGABLE  = "6";
   
   /*** ATRIBUTOS UTILIZADOS PARA RECUPERAR PROPIEDADES DE CONFIGURACIÓN UTILIZADAS POR LOS MÓDULOS DE INTEGRACIÓN EXTERNOS ***/
   public static final String OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_DESPLEGABLE = "OPERACIONES/OBLIGATORIAS/GRABACION/CAMPOSUPLEMENTARIO_DESPLEGABLE/";
   public static final String OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_NUMERICO    = "OPERACIONES/OBLIGATORIAS/GRABACION/CAMPOSUPLEMENTARIO_NUMERICO/";
   public static final String OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_TEXTO       = "OPERACIONES/OBLIGATORIAS/GRABACION/CAMPOSUPLEMENTARIO_TEXTO/";
   public static final String OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_TEXTO_LARGO = "OPERACIONES/OBLIGATORIAS/GRABACION/CAMPOSUPLEMENTARIO_TEXTO_LARGO/";
   public static final String OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_FECHA       = "OPERACIONES/OBLIGATORIAS/GRABACION/CAMPOSUPLEMENTARIO_FECHA/";
   public static final String OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_FICHERO     = "OPERACIONES/OBLIGATORIAS/GRABACION/CAMPOSUPLEMENTARIO_FICHERO/";

   public static final String SUFIJO_EXPEDIENTE                                    = "EXPEDIENTE";
   public static final String SUFIJO_TRAMITE                                       = "TRAMITE";
   public static final String COMMA_SIMPLE                                         = ",";
   public static final String FICHERO_CONFIGURACION_MODULOS_INTEGRACION            = "ModulosIntegracion";

   public static final String MODULOS_INTEGRACION_ACTIVOS                          = "/MODULO_INTEGRACION/ACTIVOS";   
   public static final String MODULO_INTEGRACION                                   = "/MODULO_INTEGRACION/";
   public static final String MODULO_IMPL_CLASS                                    = "/IMPLCLASS";
   public static final String URL_PANTALLA_CONFIGURACION_OPERACION_MODULO          = "/URL_PANTALLA_CONFIGURACION";
   public static final String URL_PANTALLA_TRAMITACION_OPERACION_MODULO            = "/URL_PANTALLA_TRAMITACION";
   public static final String DESCRIPCION_MODULO                                   = "/DESCRIPCION_MODULO";
   public static final String ALTO_PANTALLA_CONFIGURACION_OPERACION_MODULO         = "/ALTO_PANTALLA_CONFIGURACION";
   public static final String ANCHO_PANTALLA_CONFIGURACION_OPERACION_MODULO        = "/ANCHO_PANTALLA_CONFIGURACION";
   public static final String LISTA_OPERACIONES_DISPONIBLES_MODULO                 = "LISTA_OPERACIONES_DISPONIBLES";
   public static final String PANTALLA_TRAMITACION_ETIQUETA_IDIOMA                 = "/PANTALLA_TRAMITACION_ETIQUETA_IDIOMA";

   public static final String URL_PANTALLA_EXPEDIENTE_OPERACION_MODULO             = "/URL_PANTALLA_EXPEDIENTE";
   public static final String URL_PANTALLA_EXPEDIENTE_RELACION_OPERACION_MODULO    = "/URL_PANTALLA_EXPEDIENTE_RELACION";
   public static final String PANTALLA_EXPEDIENTE_ETIQUETA_IDIOMA                  = "/PANTALLA_EXPEDIENTE_ETIQUETA_IDIOMA";
   public static final String PREPARAR_PANTALLA_URL                                = "/PREPARAR_PANTALLA_URL";
   public static final String ACTUALIZAR_PANTALLA_EXPEDIENTE                       = "/ACTUALIZAR_PANTALLA_EXPEDIENTE";
   public static final String ACTUALIZAR_PANTALLA_TRAMITE                          = "/ACTUALIZAR_PANTALLA_TRAMITE";
   public static final String ONLOAD_PANTALLA_TRAMITE                              = "/ONLOAD_PANTALLA_TRAMITE";
   
   public static final String OPERACION_PROCESAR                                   = "/OPERACION_PROCESAR";
   public static final String OPERACION_PREPARAR                                   = "/OPERACION_PREPARAR";
   public static final String FILE_CONFIG                                          = "/FILE_CONFIG";

   public static final String CON_GESTOR                                           = "CON.gestor";
   public static final String PROPIEDAD_EDITOR_PLANTILLAS                          = "editorPlantillas";
   public static final String OOFFICE                                              = "OOFFICE";
   public static final String WORD                                                 = "WORD";

   public static final String MENSAJE_ERROR_MODULO_EXTERNO                         = "/MENSAJE_ERROR/";

   public static final String PANTALLA_EXPEDIENTE_OPERACION_PROCESAR               = "/PANTALLA_EXPEDIENTE/OPERACION_PROCESAR";
   public static final String PANTALLA_EXPEDIENTE_RELACION_OPERACION_PROCESAR      = "/PANTALLA_EXPEDIENTE_RELACION/OPERACION_PROCESAR";
   public static final String PANTALLA_EXPEDIENTE_NOMBRE_PANTALLAS                 = "/PANTALLA_EXPEDIENTE/NOMBRE_PANTALLAS";
   public static final String PANTALLA_EXPEDIENTE_RELACION_NOMBRE_PANTALLAS        = "/PANTALLA_EXPEDIENTE_RELACION/NOMBRE_PANTALLAS";
   public static final String PANTALLA_TRAMITE_NOMBRE_PANTALLAS                    = "/PANTALLA_TRAMITE/NOMBRE_PANTALLAS";

   public static final String PANTALLA_TRAMITE_OPERACION_PROCESAR                  = "/PANTALLA_TRAMITE/OPERACION_PROCESAR";
   public static final String PANTALLA_TRAMITE_RELACION_NOMBRE_PANTALLAS           = "/PANTALLA_TRAMITE_RELACION/NOMBRE_PANTALLAS";
   public static final String PANTALLA_TRAMITE_RELACION_OPERACION_PROCESAR         = "/PANTALLA_TRAMITE_RELACION/OPERACION_PROCESAR";
   public static final String URL_PANTALLA_TRAMITACION_RELACION                    = "/URL_PANTALLA_TRAMITACION_RELACION";

   public static final String ACTUALIZAR_PANTALLA_TRAMITE_RELACION                 = "/ACTUALIZAR_PANTALLA_TRAMITE_RELACION";

   public static final String TIPO_ORIGEN_OPERACION_WS      = "WS";
   public static final String TIPO_ORIGEN_OPERACION_MODULO  = "MODULO";

   public static final String OPERACION  = "@OPERACION";
   public static final String OBJETO     = "@OBJETO";
   public static final String CAMPO      = "@CAMPO";

   public static final String JUSTIFICANTE_ACTIVO                              = "SI";
   public static final String JUSTIFICANTE_NO_ACTIVO                           = "NO";
   public static final String EXTENSION_JUSTIFICANTE_REGISTRO_JASPER           = ".jasper";
   public static final String EXTENSION_JUSTIFICANTE_REGISTRO_ZIP              = ".zip";
   public static final String TAM_MAX_JUSTIFICANTE_REGISTRO                    = "/TAM_MAX_JUSTIFICANTE_REGISTRO";
   public static final String EXTENSION_JUSTIFICANTE_REGISTRO_OPPENOFFICE           = ".odt";
   
     public static final String MODELO_PETICION_RESPUESTA                        = "/MODELO_PETICION_RESPUESTA";	
   // Tipos de justificantes	
   public static final String TIPO_JUSTIFICANTE_MODELO_PETICION_RESPUESTA      = "MODELO_PETICION_RESPUESTA";
   
   public static final String ERROR_JUSTIFICANTE_REGISTRO_EXTENSION_INCORRECTA = "1";
   public static final String ERROR_CONNECTION_JUSTIFICANTE_REGISTRO           = "2";
   public static final String ERROR_TECNICO_ALTA_JUSTIFICANTE_REGISTRO         = "3";
   public static final String ERROR_COPIAR_JUSTIFICANTE_REGISTRO               = "4";
   public static final String ERROR_BBDD_EXISTE_JUSTIFICANTE_REGISTRO          = "5";
   public static final String ERROR_NO_EXISTE_DIRECTORIO_PLANTILLAS_JUSTIFICANTE = "6";
   public static final String ERROR_JUSTIFICANTE_ELIMINAR_DESCONOCIDO            = "7";
   public static final String ERROR_ELIMINAR_DIRECTORIO_JUSTIFICANTE_DESCONOCIDO = "8";
   public static final String ERROR_ELIMINAR_JUSTIFICANTE_NO_EXISTE_DISCO       = "9";
   public static final String JUSTIFICANTE_ELIMNADO_CORRECTAMENTE              = "10";
   public static final String ERROR_ELIMINAR_JUSTIFICANTE_BBDD                 = "11";
   public static final String ERROR_ELIMINAR_JUSTIFICANTE_SERVIDOR             = "12";
   public static final String ERROR_OBTENER_CONEXION_BBDD                      = "13";
   public static final String ERROR_TECNICO_EJECUCION_ELIMINAR_JUSTIFICANTE    = "14";

   public static final String ERROR_ALTA_JUSTIFICANTE_DESCONOCIDO              = "15";
   public static final String ERROR_PROPIEDAD_TAM_MAX_JUSTIFICANTE             = "16";
   public static final String ERROR_TAM_MAX_JUSTIFICANTE_EXCEDIDO              = "17";
   public static final String ERROR_PROPIEDAD_RUTA_DIRECTORIO_JUSTIFICANTES    = "18";
   public static final String JUSTIFICANTE_ALTA_CORRECTAMENTE                  = "19";
   public static final String JUSTIFICANTE_ACTIVAR_DESCONOCIDO                 = "20";
   public static final String JUSTIFICANTE_ACTIVADO_CORRECTAMENTE              = "21";
   public static final String JUSTIFICANTE_ACTIVADO_INCORRECTAMENTE            = "22";
   public static final String JUSTIFICANTE_ACTIVADO_CONEXION_BD_INCORRECTA     = "23";
   public static final String ERROR_TECNICO_EJECUCION_ACTIVAR_JUSTIFICANTE     = "24";
   public static final String ERROR_CONNECTION_GETJUSTIFICANTES                = "25";
   public static final String ERROR_TECNICO_EJECUCION_GETJUSTIFICANTES         = "26";
   public static final String ERROR_EXISTE_JUSTIFICANTE_ACTIVADO               = "27";
   public static final String ERROR_JUSTIFICANTE_DESACTIVAR_DESCONOCIDO        = "28";
   public static final String JUSTIFICANTE_DESACTIVADO_CORRECTAMENTE           = "29";
   public static final String JUSTIFICANTE_DESACTIVADO_INCORRECTAMENTE         = "30";
   public static final String ERROR_CONNECTION_DESACTIVAR_JUSTIFICANTE         = "31";
   public static final String ERROR_TECNICO_EJECUCION_DESACTIVAR_JUSTIFICANTE  = "32";
   public static final String FORMATO_ZIP_JUSTIFICANTE_NO_VALIDO               = "33";
   public static final String FICHEROS_JUSTIFICANTE_EXISTEN_DESTINO            = "34";
   public static final String ERROR_TECNICO_VALIDACION_FICHEROS_DIRECTORIO_DESTINO = "35";
   public static final String ERROR_TECNICO_DESCOMPRESION_ZIP                      = "36";

   public static final String RUTA_PLANTILLAS_JUSTIFICANTE                     = "/RUTA_PLANTILLAS_JUSTIFICANTE";
   public static final String JUSTIFICANTE_REGISTRO_IMPL_CLASS                 = "/JUSTIFICANTE_REGISTRO/IMPL_CLASS";
   public static final String ZERO                                             = "0";
   public static final int LONGITUD_MAXIMA_NUMERO_ASIENTO                      = 6;

   public static final String ALTA_TERCERO_EXTERNO                             = "AltaTerceroExterno/";
   public static final String ALTA_TERCERO_EXTERNO_IMPLCLASS                   = "/implClassName";
   public static final String ALTA_TERCERO_EXTERNO_SERVICIOS_DISPONIBLES       = "/serviciosDisp";
   public static final String ALTA_TERCERO_EXTERNO_TIPOS_DOCS_VALIDOS_ALTA     = "/tipos_documentos_flexia_validos_alta";
   public static final String ALTA_TERCERO_EXTERNO_TIPOS_DOCS_VALIDOS_MOD      = "/tipos_documentos_flexia_validos_modificar";
   public static final String ALTA_TERCERO_TRANSACCIONAL                       = "/transaccional";

   public static final String CAMPOS_SUPLEMENTARIOS_TERCERO                    ="CamposSuplementariosTercero/";
   public static final String ACTIVAR                                          ="/activar";

   public static final String RESTRICCIONES_ALTA_TERCERO                       = "/RestriccionesAltaTercero/CodigoAplicaciones";

   public static final String CARGAR_NUEVOS_CRITERIOS_BUSQUEDA                 = "/CARGAR_NUEVOS_CRITERIOS_BUSQUEDA";

   public static final String FORMATO_ALTERNATIVO_FECHAS_DOC_TRAMITACION       = "/FORMATO_ALTERNATIVO_FECHAS_DOC_TRAMITACION";
   public static final String SUFIJO_ETIQUETA_FEC_ALTERNATIVA                  = "alt";
   public static final String SUFIJO_NOMBRE_ETIQUETA_FEC_ALTERNATIVA           = " FORMATO ALTERNATIVO";

   public static final String TIPO_DATO_FECHAS_ETIQUETAS_DOCS_TRAMITACION      = "D";
   public static final String TIPO_PLANTILLA_CAMPOS_SUPLEMENTARIOS_FECHA       = "4";

   public static final String TIPO_MIME_INCORRECTO ="TIPO_MIME_INCORRECTO";
   public static final String TIPOS_MIME_VALIDOS   ="TIPOS_MIME_VALIDOS";
   public static final String FICHERO_EXISTE       ="FICHERO_EXISTE";
   
   
   public static final String ESTADO_FIRMA_PENDIENTE ="O";
   public static final String ESTADO_FIRMA_FIRMADO ="F";
   public static final String ESTADO_FIRMA_RECHAZADO="R";
   public static final String ESTADO_FIRMA_TIPO_FLUJO="L";
   public static final String ESTADO_FIRMA_TIPO_FLUJO_ORIGINAL="M";
   public static final String ESTADO_FIRMA_TIPO_USUARIO="U";
   public static final String ESTADO_FIRMA_TIPO_USUARIO_ORIGINAL="V";
   
   public static final int TIPO_CERTIFICADO_ORGANISMO = 0;
   public static final int TIPO_CERTIFICADO_USUARIO   = 1;
   
   public static final String URL_PANTALLA_PROCEDIMIENTO_OPERACION_MODULO             = "/PANTALLA_DEFINICION_PROCEDIMIENTO/URL";
   public static final String PANTALLA_DEFINICION_PROCEDIMIENTO_NOMBRE_PANTALLAS      = "/PANTALLA_DEFINICION_PROCEDIMIENTO/NOMBRE_PANTALLAS";
   public static final String PANTALLA_DEFINICION_PROCEDIMIENTO_OPERACION_PROCESAR    = "/PANTALLA_DEFINICION_PROCEDIMIENTO/PROCESO";
   
   public static final String METODOS_PERSONA_FISICA                                  ="/METODOS_PERSONA_FISICA";
   public static final String CAMPOS_SUPLEM_TERCERO                                   ="/CAMPOS_SUPLEM_TERCERO";
   public static final String LANBIDE                                                 ="/LANBIDE";
   public static final String OBLIGATORIO_ASUNTO_CODIFICADO_REGISTRO = "/asunto_codificado_registro_obligatorio";
   
   public static final String BUSQUEDA_AJAX_TERCERO = "/busquedaRegistroPorTercero";
   
   public static final String PROCEDIMIENTO_IMPRESION ="/PROCEDIMIENTO_IMPRESION";
   public static final String CODIGO_TRAMITE_PENDIENTE ="/CODIGO_TRAMITE_PENDIENTE";
   public static final String CODIGO_TRAMITE_AVANZAR ="/CODIGO_TRAMITE_AVANZAR";
   public static final String CODIGO_CAMPO_TSEXOTERCERO ="/CODIGO_CAMPO_TSEXOTERCERO";
   public static final String CODIGO_CAMPO_TFECHANACIMIENTO ="/CODIGO_CAMPO_TFECHANACIMIENTO";
   public static final String CODIGO_CAMPO_TSEXOTERCERO_HOMBRE ="/CODIGO_CAMPO_TSEXOTERCERO_HOMBRE";
   public static final String CODIGO_CAMPO_TSEXOTERCERO_MUJER ="/CODIGO_CAMPO_TSEXOTERCERO_MUJER";
   public static final String VALOR_CAMPO_TSEXOTERCERO_HOMBRE ="/VALOR_CAMPO_TSEXOTERCERO_HOMBRE";
   public static final String VALOR_CAMPO_TSEXOTERCERO_MUJER ="/VALOR_CAMPO_TSEXOTERCERO_MUJER";
   public static final String VALOR_IMPRESION_CEPAP ="/VALOR_IMPRESION_CEPAP";
   public static final String CODIGOS_UNIDADES_ORGANICAS_VALIDAS = "/CODIGOS_UNIDADES_ORGANICAS_VALIDAS";
   
   //Indica si se pueden mantener las anotaciones en el buzon de entrada para permitir asociar o iniciar varios expedientes con una misma anotación
   public static final String PERMANENCIA_ANOTACION_BUZON_ENTRADA = "/ACTIVAR_PERMANENCIA_ANOTACION_BUZON_ENTRADA";
   
   //Indica los procedimientos que aunque se permita mantener las asociaciones en el buzón, para estos códigos de procedimiento no será posible.
   public static final String PROCEDIMIENTOS_RESTRINGIDOS_PERMANENCIA_ANOTACION = "/PROCEDIMIENTOS_RESTRINGIDOS_PERMANENCIA_ANOTACION_BUZON_ENTRADA";
   
   public static final String SERVICIO_VALIDO_PERMANENCIA_BUZON_ENTRADA = "/SERVICIO_VALIDO_PERMANENCIA_ANOTACION_BUZON_ENTRADA";
   
 //Constantes para la nueva funcionalidad de campos de Consulta de módulos externos
   public static final String CAMPOS_CONSULTA="/CAMPOS_CONSULTA";
   public static final String CONSULTA="/CONSULTA/";
   public static final String ROTULO_CAMPOS_CONSULTA="/ROTULO";
   public static final String SQL_CAMPOS_CONSULTA="/SQL";
   public static final String TAMANHO_CAMPOS_CONSULTA="/TAMANHO";
   public static final String TIPO_CAMPOS_CONSULTA="/TIPO";
   public static final String TIPO_CAMPOS_CONSULTA_CODIGOS="/VALORES/CAMPO_CODIGO_TABLA_BD";
   public static final String TIPO_CAMPOS_CONSULTA_DESCRIPCIONES="/VALORES/CAMPO_DESCRIPCION_TABLA_BD";
   public static final String TIPO_CAMPOS_CONSULTA_NOMBRE_TABLA="/VALORES/NOMBRE_TABLA";
   //Constante para la nueva funcionalidad de etiquetas de los Módulos de Consulta
   //Se refiere a los distintos tipos de plantilla, para crear un documento
   public static final String PLANTILLA_I="/PLANTILLA_INTERESADO";
   public static final String PLANTILLA_N="/PLANTILLA_NORMAL";
   public static final String PLANTILLA_R="/PLANTILLA_RELACION";
   public static final String ETIQUETAS="/ETIQUETAS";
   public static final String NOMBRE_ETIQUETA="/NOMBRE_ETIQUETA";
   public static final String TIPO_ETIQUETA="/TIPO_ETIQUETA";
   public static final String SQL_ETIQUETA="/SQL_ETIQUETA";
   public static final String CODIGO_ETIQUETA="/CODIGO_ETIQUETA";
   public static final String SQL_NOMBRE_COLUMNA="/SQL_NOMBRE_COLUMNA";
   public static final String CAMPOE="/CAMPO";
   public static final String CLAUSULA_WHERE="/CLAUSULA_WHERE";
   public static final String CLAUSULA_AND1="/CLAUSULA_AND1";
   public static final String CLAUSULA_AND2="/CLAUSULA_AND2";
   public static final String CLAUSULA_AND3="/CLAUSULA_AND3";
   public static final String FORMATO_FECHA="/FORMATO_FECHA";
   public static final String FORMATO_NUMERO="/FORMATO_NUMERO";
   
   //Ahora la fecha del sello de registro, puede venir en distintos formatos
   // es decir, dd/MM/yyyy o yyyy/MM/dd
   public static final String FORMATO_FECHA_SELLO_REGISTRO="/FORMATO_FECHA_SELLO_REGISTRO";
   
   //Nombre del fichero XML
   public static final String NOMBRE_FICHERO_DATOS_INTEGRACION_SOLICITUD = "FLX_DATOS_INTEGRACION_SOLICITUD";
   
   //Nombre de tags XML
   public static final String TAG_XML_CAMPOSVARIABLES = "CamposVariables";
   public static final String TAG_XML_CAMPO = "Campo";
   public static final String TAG_XML_CODCAMPO = "CodCampo";
   public static final String TAG_XML_VALORCAMPO = "ValorCampo";
   public static final String TAG_XML_INTERESADOS = "Interesados";
   public static final String TAG_XML_INTERESADO = "Interesado";
   public static final String TAG_XML_NIF = "Nif";        
   public static final String TAG_XML_ROL = "Rol";
   public static final String TAG_XML_NOMBRE = "Nombre";
   public static final String TAG_XML_APELLIDO1 = "Apellido1";
   public static final String TAG_XML_APELLIDO2 = "Apellido2";
   public static final String TAG_XML_TELEFONO = "Telefono";
   public static final String TAG_XML_EMAIL = "Email";
   public static final String TAG_XML_NOTIFELECTRONICA = "NotificacionElectronica";   

   public static final String TAG_XML_DOMICILIO = "Domicilio";   
   public static final String TAG_XML_PROVINCIA = "Provincia";     
   public static final String TAG_XML_MUNICIPIO = "Municipio";
   public static final String TAG_XML_CODVIA = "CodigoVia";     
   public static final String TAG_XML_TIPOVIA = "TipoVia";     
   public static final String TAG_XML_NOMBREVIA = "NombreVia"; 
   public static final String TAG_XML_EMPLAZAMIENTO = "Emplazamiento";    
   public static final String TAG_XML_LETRADESDE = "LetraDesde";     
   public static final String TAG_XML_LETRAHASTA = "LetraHasta";     
   public static final String TAG_XML_BLOQUE = "Bloque";        
   public static final String TAG_XML_PORTAL = "Portal";     
   public static final String TAG_XML_ESCALERA = "Escalera";     
   public static final String TAG_XML_PLANTA = "Planta";        
   public static final String TAG_XML_PUERTA = "Puerta";     
   public static final String TAG_XML_CODIGOPOSTAL = "CodigoPostal";     
   public static final String TAG_XML_NUMERODESDE = "NumeroDesde";        
   public static final String TAG_XML_NUMEROHASTA = "NumeroHasta";   
   public static final String TAG_XML_NOTIFICACIONELECTRONICA = "NotificacionElectronica"; 
   
   public static final Integer NUMERO_TAGS_INTERESADO_PRINCIPAL = 2;
   
   public static final String TAG_XML_DATOSSUPLEMENTARIOS = "DatosSuplementarios";   
   
   public static final String TAG_XML_CODIGO = "Codigo";
   public static final String TAG_XML_VALOR = "Valor";
   public static final String TAG_XML_TRAMITE = "Tramite";
   
   public static final String TAG_XML_FLX_EXTENSION = "FLX_EXTENSION";
   public static final String TAG_XML_CODIGOMODULO = "CodigoModulo";
   public static final String TAG_XML_EXPEDIENTE_RELACIONADO = "ExpedienteRelacionado";
   
   public static final String ATRIBUTO_XML_COD = "cod";
   
   public static final String INTERESADO_PRINCIPAL = "1";
      
   public static final Integer REGISTRO_GENERA_EXPEDIENTE = 0;
   
   public static final Integer TIPO_DOCUMENTO_SIN_DOCUMENTO = 0;
   public static final Integer TIPO_DOCUMENTO_NIF = 1;
   public static final Integer TIPO_DOCUMENTO_CIF = 4;
   public static final Integer TIPO_DOCUMENTO_NIE = 3;           
 
   public static final String CODIGO_PAIS_ESPAÑA = "108";
   
   public static final int TIPO_VIA_CALLE = 18;
   public static final int TIPO_VIA_SINVIA = 0;
   
   
   
   // Estados para los documentos asociados a una anotación de registro
   public static final int ESTADO_DOCUMENTO_NUEVO     = 0;
   public static final int ESTADO_DOCUMENTO_ELIMINADO = 1;
   public static final int ESTADO_DOCUMENTO_GRABADO   = 2;
   public static final int ESTADO_DOCUMENTO_VACIO = 3;
   public static final int ESTADO_DOCUMENTO_MODIFICADO=4;
   
   public static final String RUTA_FICHERO_SUBIDO_SERVIDOR   = "RUTA_FICHERO_SUBIDO_SERVIDOR";
   public static final String SUBCARPETA_DOCUMENTOS_REGISTRO = "REGISTRO";   
   public static String ERROR_COPIAR_DOCUMENTO_REGISTRO_RUTA_DISCO = "ERROR_COPIAR_DOCUMENTO_REGISTRO_RUTA_DISCO";
   public static String ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO = "ERROR_OBTENER_RUTA_ALMACEN_TEMPORAL_DISCO";
   
   public static final String SUBCARPETA_DOCUMENTOS_PLANTILLA = "PLANTILLA"; 
   
   

   public static final String DESCRIPCION_ENTRADAS_REGISTRO = "ENTRADAS";
   public static final String DESCRIPCION_SALIDAS_REGISTRO  = "SALIDAS";
   public static final String SUBCARPETA_DOCUMENTOS_EXPEDIENTE = "EXPEDIENTE";      
   public static final String SUBCARPETA_DOCUMENTOS_TRAMITE = "TRAMITE";   
   public static final String SUBCARPETA_DOCUMENTOS_CSV = "CSV";
   
   
   public static final String ORIGEN_ELIMINAR_DOCUMENTO_EXPEDIENTE = "EXPEDIENTE";
   public static final String ORIGEN_ELIMINAR_DOCUMENTO_TRAMITE    = "TRAMITE";
   public static final String CARPETA_DOCUMENTOS_EXTERNOS_NOTIFICACION = "EXTERNOS_NOTIFICACION";
   
   public static final Integer ESTADO_EXPEDIENTE_PENDIENTE = 0;
   public static final Integer ESTADO_EXPEDIENTE_ANULADO = 1;
   public static final Integer ESTADO_EXPEDIENTE_FINALIZADO = 9;
   
   public static final String DESDE_JSP_FICHA_EXPEDIENTE = "si";
   public static final String PLUGIN_ALMACENAMIENTO_BBDD   = "BBDD";
   public static final String PLUGIN_ALMACENAMIENTO_ALFRESCO = "ALFRESCO";   
   public static final String PLUGIN_ALMACENAMIENTO_NEXTRET = "NEXTRET";
   
   public static final int ID_PLUGIN_IMPLCLASS_PLUGIN_ALMACENAMIENTO_BBDD = 1;
   public static final String NOMBRE_PLUGIN_ALMACENAMIENTO_BBDD = "BBDD";
   public static final String IMPLCLASS_PLUGIN_ALMACENAMIENTO_BBDD = "es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoBBDDImpl";
   
   public static final int TIPO_CAMPO_NUMERICO = 1;
   public static final int TIPO_CAMPO_TEXTO = 2;
   public static final int TIPO_CAMPO_FECHA = 3;
   public static final int TIPO_CAMPO_TEXTO_LARGO = 4;
   public static final int TIPO_CAMPO_FICHERO = 5;
   public static final int TIPO_CAMPO_DESPLEGABLE= 6;
   public static final int TIPO_CAMPO_NUMERICO_CALCULADO= 8;
   public static final int TIPO_CAMPO_FECHA_CALCULADO= 9;
   public static final int TIPO_CAMPO_DESPLEGABLE_EXTERNO = 10;

   public static final String TIPO_CAMPO_FICHERO_STR = "5";
   public static final String SI = "SI";
   public static final String NO = "NO";
	public static final String S = "S";
	public static final String N = "N";
   public static final String XPDL_EXPORTACION_N = "N"; 
   public static final String XPDL_EXPORTACION_S = "S";
   public static final String XPDL_FIRMA_TRAMITADOR = "T"; 
   public static final String XPDL_FIRMA_UN_USUARIO = "U"; 
   public static final String XPDL_FIRMA_OTRO_USUARIO = "O"; 
   public static final String XPDL_FIRMA_FLUJO = "L"; 
   
   public static final String NOTIFICACIONES_SOLO_LECTURA = "NOTIFICACIONES_SOLO_LECTURA";
   public static final String CHECK_NOTIFICACIONES_ACTIVO = "CHECK_NOTIFICACIONES_ACTIVO_DEFECTO";
   public static final String ORACLE    = "ORACLE";
   public static final String SQLSERVER = "SQLSERVER";
   
   public static final String ACTIVO    = "ACTIVO";
   public static final String HISTORICO = "HISTORICO";
   
   public static final String MOSTRAR_UBICACION_DOCUMENTACION = "MOSTRAR_UBICACION_DOCUMENTACION";
   public static final String ELEGIR_ORGANIZACION_IMPORTAR_PROCEDIMIENTO = "/DEFINICION_PROCEDIMIENTO/ELEGIR_ORGANIZACION_IMPORTAR";
   
   /* Constantes para el registro de acceso de usuarios */
   public static final int CODIGO_ACCESO_FLEXIA = 0;
   public static final String DESCRIPCION_ACCESO_FLEXIA = "LOGIN";
   
   /* Rutas internas */

   public static final String RUTA_PLANTILLAS_JASPER = "pdf" + File.separator + "jasper";
	
   /* Plantillas */
   public static final String FICHERO_PLANTILLA_LISTADO_ANOTACIONES = "report_listaRegistroAnotacion.jasper";
   public static final String FICHERO_PLANTILLA_EXCEL_INFORMES = "report_gestionInformesExcel.jrxml";

   //Tipos de movimiento
   public static final int TIPO_MOV_ALTA_EXPEDIENTE = 0;
   public static final int TIPO_MOV_GRABAR_EXPEDIENTE = 1;
   public static final int TIPO_MOV_GRABAR_TRAMITE = 2;
   public static final int TIPO_MOV_AVANZAR_TRAMITE = 3;
   public static final int TIPO_MOV_RETROCEDER_TRAMITE = 4;
   public static final int TIPO_MOV_FINALIZAR_EXPEDIENTE = 5;
   public static final int TIPO_MOV_ANULAR_EXPEDIENTE = 6;
   public static final int TIPO_MOV_ALTA_INTERESADO = 7;
   public static final int TIPO_MOV_MODIFICAR_INTERESADO = 8;          
   public static final int TIPO_MOV_ELIMINAR_INTERESADO = 9;  
   public static final int TIPO_MOV_REABRIR_EXPEDIENTE = 10;
   public static final int TIPO_MOV_ANHADIR_RELACION = 11;
   public static final int TIPO_MOV_ELIMINAR_RELACION = 12;
   public static final int TIPO_MOV_ALTA_DOC_EXP = 13;
   public static final int TIPO_MOV_ELIMINAR_DOC_EXP = 14;
   public static final int TIPO_MOV_ALTA_DOC_EXP_EXT = 15;
   public static final int TIPO_MOV_ELIMINAR_DOC_EXP_EXT = 16;
   public static final int TIPO_MOV_ALTA_DOC_TRAMITE = 17;
   public static final int TIPO_MOV_ELIMINAR_DOC_TRAMITE = 18;
   public static final int TIPO_MOV_ALTA_DOC_SUPL_TRAMITE = 19;
   public static final int TIPO_MOV_CAMBIO_DOM_INTERESADO = 20;
   public static final int TIPO_MOV_GRABAR_CAMPOS_EXPEDIENTE = 21;
   public static final int TIPO_MOV_GRABAR_CAMPOS_TRAMITE = 22;
   public static final int TIPO_MOV_INICIAR_TRAMITE = 23;
   public static final int TIPO_MOV_INICIAR_TRAMITE_MANUAL = 24;
   public static final int TIPO_MOV_RETROCEDER_TRAMITE_ORIGEN = 25;
   public static final int TIPO_MOV_BLOQUEAR_TRAMITE = 26;
   public static final int TIPO_MOV_DESBLOQUEAR_TRAMITE = 27;
   public static final int TIPO_MOV_CAMBIO_UTR = 28;
   public static final int TIPO_MOV_IMPORTAR_EXPEDIENTE = 29;
   public static final int TIPO_MOV_EXPORTAR_EXPEDIENTE = 30;
   public static final int TIPO_MOV_REABRIR_TRAMITE = 31;
   public static final int TIPO_MOV_ANULAR_DEUDA = 32;
   public static final int TIPO_MOV_BORRAR_CAMPOS_TRAMITE  = 33;
   
   //Tipos de alta de expediente
   public static final String TIPO_ALTA_EXP_NORMAL = "0";
   public static final String TIPO_ALTA_EXP_ASIENTO = "1";
   public static final String TIPO_ALTA_EXP_COPIA = "2";
   public static final String TIPO_ALTA_EXP_ASOCIADO = "3";
   public static final String TIPO_ALTA_EXP_WEBSERVICE_FLEXIA = "4";
   
     // Origen de la llamada efectuada	
   public static final String ORIGEN_LLAMADA_WEB_SERVICE = "SW";	
   public static final String ORIGEN_LLAMADA_INTERFAZ_WEB = "IN";	
   public static final String ORIGEN_LLAMADA_NOMBRE_PARAMETRO = "origenLlamada";
   
   // plugin Notificaciones
   public static final String NOTIFICACIONES_IMPLCLASS = "Notificacion/ALTIA/implClass";
   public static final String NOTIFICACIONES_PLATEA = "PluginNotificacionPlatea";
   
   /* Constantes para los metadatos de los documentos cotejados */
   public static final int ID_ORIGEN_CIUDADANO = 0;
   public static final int ID_ORIGEN_ADMINISTRACION = 1;
   
   public static final String DESC_ORIGEN_CIUDADANO = "CIUDADANO";
   public static final String DESC_ORIGEN_ADMINISTRACION = "ADMINISTRACION";
   
   public static final String METADATO_COTEJO_VERSION_NTI = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e";
	
   public static final String METADATO_COTEJO_NOMBRE_FORMATO = "PAdES";
	
      
      // Constantes para el fichero PDF de los documentos cotejados	
   public static final String EXTENSION_FICHERO_TMP_COTEJO = "CTJ";
   public static final String PREFIJO_FICHERO_TMP_CSV = "CSV_";
   public static final String SUFIJO_FICHERO_TMP_CSV = "_conv";
   public static final String EXTENSION_FICHERO_TMP_CSV = "TMP";
   public static final String PREFIJO_FICHERO_JUSTIFICANTE_CSV = "JE_";
   
      // Constantes de codificacion de caracteres

   public static final String CHARSET_UTF_8 = "UTF-8";
   
   // Proveedores de seguridad
   public static final String SECURITY_PROVIDER_BOUNCYCASTLE_CLASS = "org.bouncycastle.jce.provider.BouncyCastleProvider";
   public static final String SECURITY_PROVIDER_BOUNCYCASTLE = "BC";
   
   // Subconsultas utilizadas para las consultas de los metadatos de documento
   public static final String SUBCONSULTA_E_EXT_DOC_ID_METADATO_CODIGO_INTERNO = " SELECT DOC_EXT_ID_METADATO FROM E_DOC_EXT WHERE DOC_EXT_COD=? ";
   public static final String SUBCONSULTA_E_TFI_ID_METADATO_PK = " SELECT TFI_ID_METADATO FROM E_TFI WHERE TFI_COD = ? AND TFI_EJE = ? AND TFI_MUN = ? AND TFI_NUM = ? ";
   public static final String SUBCONSULTA_E_TFIT_ID_METADATO_PK = " SELECT TFIT_ID_METADATO FROM E_TFIT WHERE TFIT_COD = ? AND TFIT_EJE = ? AND TFIT_MUN = ? AND TFIT_NUM = ? AND TFIT_TRA = ? AND TFIT_OCU = ? ";
   public static final String SUBCONSULTA_E_CRD_ID_METADATO_PK = " SELECT CRD_ID_METADATO FROM E_CRD WHERE CRD_MUN = ? AND CRD_PRO = ? AND CRD_EJE = ? AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? AND CRD_NUD = ? ";
   
   // Aplicaciones generadoras
   public static final String APLICACION_ORIGEN_DOCUMENTO_FLEXIA = "SGE";
   
   // Prefijo para codigos de VerDocumentos
   public static final String VER_DOCUMENTO_SERVLET_PREFIJO_CODIGO_DOCUMENTO_TRAMITE = "DOC";
   
   // Estados de la tabla AUTH_EXTERNOS
   public static final Integer ESTADO_AUTH_EXTERNOS_INACTIVO = 0;
   public static final Integer ESTADO_AUTH_EXTERNOS_ACTIVO = 1;
   public static final Integer ESTADO_AUTH_EXTERNOS_BLOQUEADO = 2;

}//class
