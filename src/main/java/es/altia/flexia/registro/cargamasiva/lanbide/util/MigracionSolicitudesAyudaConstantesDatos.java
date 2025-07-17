package es.altia.flexia.registro.cargamasiva.lanbide.util;

public class MigracionSolicitudesAyudaConstantesDatos {
    // Datos necesarios en la migracion de solicitudes de ayuda de ATASE

    public static final Integer CODDEPARTAMENTO = 1;
    public static final Integer CODUNIDADREGISTRO = 0;
    public static final String TIPOANOTACION = "E";
	
    // Unidad de destino (ATASE,ACASE) es fomento del empleo DESA 8, PRE Y PRO 102
    //public static final Integer CODUNIDADDESTINOATASE = 8;//DESA
    public static final Integer CODUNIDADDESTINOATASE = 102;//PRE, PRO
    
    // Unidad de destino (AERTE) es UNIDAD TRAMITADORA RGI SSCC sólo en PRO 246 (para DESA y PRE se prueba con fomento)
    //public static final Integer CODUNIDADDESTINOAERTE = 8;//DESA
    //public static final Integer CODUNIDADDESTINOAERTE = 102;//PRE
    public static final Integer CODUNIDADDESTINOAERTE = 107;//PRO - GARINT
        
    public static final String CODASUNTOCODIFICADOATASE = "FOMATASE";
    public static final String CODASUNTOCODIFICADOACASE = "FOMACASE";
    public static final String CODASUNTOCODIFICADOAERTE = "RGIAERTE";
    //public static final String CODASUNTOCODIFICADO = "QUEJA";

    public static final String EXTRACTOASUNTO = "SOLICITUD FORMULARIO WEB ";
    public static final String URLDESCARGADOCUMENTO = "http://apps.lanbide.euskadi.net/apps/DescargaArchivo?fichero=";
    public static final Integer TIPODOCUMENTO = 101; //SOLICITUD
    //public static final Integer TIPODOCUMENTO = 99; //FORMULARIOS

    public static final Integer TIPOVIA = -1; //DESCONOCIDA
    public static final String EMPLAZAMIENTO = "NO CONSTA";
    public static final Integer CODPAIS = 108; //ESPAÑA
    public static final String CODPROVINCIA = "99"; //DESCONOCIDA
    public static final String CODMUNICIPIO = "999"; //DESCONOCIDO
	
    public static final String APLICACION = "RGI";
    //public static final String APLICACION = "flexia";
    //public static final Integer ORGANIZACION = 0;
    public static final Integer ORGANIZACION = 1;
	
}
