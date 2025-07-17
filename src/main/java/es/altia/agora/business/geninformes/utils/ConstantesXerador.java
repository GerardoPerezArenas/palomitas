package es.altia.agora.business.geninformes.utils;

/**
 * @author jgd
 * Clase ConstantesXerador en el fichero ConstantesXerador.java,creado el 31-mar-2003.
 * 
 */
public final class ConstantesXerador {

	public static String[] OPERADORESCONDICIONINFORME =
		{ "=", "LIKE", "<", ">", "<>", "IS NULL", "IS NOT NULL" };

	public static final String OPXERADOR = "EJB_OPXERADORINFORMES";
	public static final int OPXERADOR_ConsultarInformesCentro = 0;
	public static final String PEST_INF_XER_XERADOR = "800100100100";
	public static final String COD_PESTANAORIXE = "cod_pestanaorixee";
	public static final String PATH_CATALOGO = "/JREntServer/lib2/catalogos/";
	public static final String NOME_CATALOGO = "xerador.cat";
	public static final es
		.altia
		.util
		.HashtableWithNull EQUIVALENCIA_TAMANOS_FONTE =
		new es.altia.util.HashtableWithNull();
	static {
		//		EQUIVALENCIA_TAMANOS_FONTE.put("8", new Float(0.11f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("9", new Float(0.12f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("10", new Float(0.14f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("11", new Float(0.16f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("12", new Float(0.17f));
		//		//		EQUIVALENCIA_TAMANOS_FONTE.put("13", new Float(0.20f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("14", new Float(0.2f));
		//		//		EQUIVALENCIA_TAMANOS_FONTE.put("15", new Float(0.24f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("16", new Float(0.22f));
		//		//		EQUIVALENCIA_TAMANOS_FONTE.put("17", new Float(0.28f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("18", new Float(0.25f));
		//		//		EQUIVALENCIA_TAMANOS_FONTE.put("19", new Float(0.32f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("20", new Float(0.28f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("22", new Float(0.3f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("24", new Float(0.33f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("26", new Float(0.36f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("28", new Float(0.39f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("30", new Float(0.42f));
		EQUIVALENCIA_TAMANOS_FONTE.put("8", new Float(0.29f));
		EQUIVALENCIA_TAMANOS_FONTE.put("9", new Float(0.32f));
		EQUIVALENCIA_TAMANOS_FONTE.put("10", new Float(0.34f));
		EQUIVALENCIA_TAMANOS_FONTE.put("11", new Float(0.4f));
		EQUIVALENCIA_TAMANOS_FONTE.put("12", new Float(0.42f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("13", new Float(0.20f));
		EQUIVALENCIA_TAMANOS_FONTE.put("14", new Float(0.5f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("15", new Float(0.24f));
		EQUIVALENCIA_TAMANOS_FONTE.put("16", new Float(0.56f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("17", new Float(0.28f));
		EQUIVALENCIA_TAMANOS_FONTE.put("18", new Float(0.64f));
		//		EQUIVALENCIA_TAMANOS_FONTE.put("19", new Float(0.32f));
		EQUIVALENCIA_TAMANOS_FONTE.put("20", new Float(0.71f));
		EQUIVALENCIA_TAMANOS_FONTE.put("22", new Float(0.77f));
		EQUIVALENCIA_TAMANOS_FONTE.put("24", new Float(0.85f));
		EQUIVALENCIA_TAMANOS_FONTE.put("26", new Float(0.93f));
		EQUIVALENCIA_TAMANOS_FONTE.put("28", new Float(0.98f));
		EQUIVALENCIA_TAMANOS_FONTE.put("30", new Float(1.06f));

	}

	// A unidade é centímetros.
	public static final float ALTURA_CABECEIRA_OFICIAL = 2.7f;

	public static final float ALTURA_DATOS_CENTRO = 2.43f;
	public static final float ALTURA_DATOS_CENTRO_HORIZONTAL = 2.91f;
	public static final float ANCHURA_DATOS_CENTRO = 20.8f;
	public static final float ANCHURA_DATOS_CENTRO_HORIZONTAL = 28.71f;

	public static final float ALTURA_TEXTO_CABECEIRA = 0.3f;
	public static final float ALTURA_CABECEIRAS_CAMPOS = 0.3f;
	public static final float SEPARACIONCAMPOSX = 0.25f;
	public static final float SEPARACIONCAMPOSY = 0.35f;
	public static final float FACTOR_CORRECCION_ANCHO = 1.0f;
	public static final float FACTOR_CORRECCION_ALTO = 1.0f;

	//public static final float FACTOR_CORRECCION_ANCHO = 1.0f/25.345f;
	//public static final float FACTOR_CORRECCION_ALTO = 1.0f/13.043f;
	public static final float ALTO_A4_CM = 29.7f;
	public static final float ANCHO_A4_CM = 21.0f;
	public static final float CM_PER_INCH = 2.54f;

	public static final char CARACTER_MAXIMO_ANCHO = 'W';
	public static final String PATHLOGDESIGNER =
		"/space/JREntServer50/logs/desg.log";
	public static final String PATHLOGCATALOGO =
		"/space/JREntServer50/logs/cat.log";

	public static final String USERID_JREPORTS_DESIGNERAPI = "jinfonet";
	public static final String CLAVE_JREPORTS_DESIGNERAPI =
		"H1ROUD4IADGQ3TTFCLYQZQHMRJMCWICOAR1XPYH5UHKDS2C";

	public static final String PATH_CATALOGO_XERADOR =
		"/space/JREntServer50/jreports/catalogos/xerador/";

	public static final String NOME_CATALOGO_XERADOR = "xerador.cat";

	public static final String CATALOGO_XERADOR_JREPORTS =
		"/catalogos/xerador/xerador.cat";
	public static final String SERVIDOR_JREPORTS =
		"http://69.50.17.43:8888/jrserver";

	public static final String PATH_SUBREPORT_DATOSCENTRO_VERTICAL =
		PATH_CATALOGO_XERADOR + "datoscentro.cls";
	public static final String PATH_SUBREPORT_DATOSCENTRO_HORIZONTAL =
		PATH_CATALOGO_XERADOR + "datoscentroHorizontal.cls";

	public static final String MIMETYPE_XLS = "application/vnd.ms-excel";
	public static final String MIMETYPE_PDF = "application/pdf";
	public static final String MIMETYPE_RTF = "application/rtf";
	public static final String MIMETYPE_TXT = "text/plain";
	
	public static final String REPORTTYPE_XLS = "4";
	public static final String REPORTTYPE_PDF = "2";
	public static final String REPORTTYPE_TXT = "3";
	public static final String REPORTTYPE_RTF = "6";

	public static final String CODIGO_CAMPO_COUNT = "900009001";

	public static final String CAMPO_ORDENACION_CURSOS = "ENSCUR.CURCOD";
	
	public static final int TAMANO_CAMPO_NUMERO_LINHA = 4;
	public static final String CABECEIRA_CAMPO_NUMERO_LINHA = "#";
	
	public static final String CODIGO_FORMATO_LISTADO="L";
	public static final String CODIGO_FORMATO_ETIQUETA="T";
	public static final String CODIGO_FORMATO_ESTATISTICA="E";
	
	
}
