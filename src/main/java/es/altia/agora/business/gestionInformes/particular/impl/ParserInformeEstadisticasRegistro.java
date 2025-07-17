package es.altia.agora.business.gestionInformes.particular.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.sqlxmlpdf.GeneralPDF;



public class ParserInformeEstadisticasRegistro {
	
    protected static Log m_Log = LogFactory.getLog(ParserInformeEstadisticasRegistro.class.getName());
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");
    

    
    public String generarInformeExcel (Collection datosInforme, UsuarioValueObject usuVO,
    		String url, String[] paramsBD) throws TechnicalException{
    	
    	
    	Vector<Vector<String>>  filas = new Vector<Vector<String>>();
		Vector<String> uors = new Vector<String>();
		DatoEstadisticaRegistro dato = new DatoEstadisticaRegistro();
		String titulo="";
		String usuario = dato.getUsuario();
		String fecha = dato.getFecha();
		
		for (Object objDato : datosInforme) {
            dato = (DatoEstadisticaRegistro) objDato;            
		}
		filas = dato.getFilas();
		uors = dato.getUors();
		titulo = dato.getTitulo();
		uors.add(0, titulo);
    	
    	HSSFWorkbook libroExcel = new HSSFWorkbook();
    	HSSFSheet hoja = libroExcel.createSheet("Informe");
    	hoja.setDefaultColumnWidth((short) 30);

    	//Creamos un estilo para la columna de los titulos
    	HSSFCellStyle estiloCeldaTitulo = libroExcel.createCellStyle();
    	estiloCeldaTitulo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	estiloCeldaTitulo.setBottomBorderColor((short) 8);
    	estiloCeldaTitulo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	estiloCeldaTitulo.setLeftBorderColor((short) 8);
    	estiloCeldaTitulo.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	estiloCeldaTitulo.setRightBorderColor((short) 8);
    	estiloCeldaTitulo.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	estiloCeldaTitulo.setTopBorderColor((short) 8);
    	estiloCeldaTitulo.setFillForegroundColor((new HSSFColor.PALE_BLUE()).getIndex());
    	estiloCeldaTitulo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    	HSSFFont fuenteCeldaTitulo = libroExcel.createFont();
    	fuenteCeldaTitulo.setFontHeightInPoints((short) 10);
    	fuenteCeldaTitulo.setFontName(HSSFFont.FONT_ARIAL);
    	fuenteCeldaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    	estiloCeldaTitulo.setFont(fuenteCeldaTitulo);
        
    	//Estilo para la 1ª fila
    	HSSFCellStyle estiloPrimeraFila = libroExcel.createCellStyle();
    	estiloPrimeraFila.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	estiloPrimeraFila.setBottomBorderColor((short) 8);
    	estiloPrimeraFila.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	estiloPrimeraFila.setLeftBorderColor((short) 8);
    	estiloPrimeraFila.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	estiloPrimeraFila.setRightBorderColor((short) 8);
    	estiloPrimeraFila.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	estiloPrimeraFila.setTopBorderColor((short) 8);
    	estiloPrimeraFila.setFillForegroundColor((new HSSFColor.PALE_BLUE()).getIndex());
    	estiloPrimeraFila.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    	estiloPrimeraFila.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	HSSFFont fuentePrimeraFila = libroExcel.createFont();
    	fuentePrimeraFila.setFontHeightInPoints((short) 10);
    	fuentePrimeraFila.setFontName(HSSFFont.FONT_ARIAL);
    	fuentePrimeraFila.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    	estiloPrimeraFila.setFont(fuentePrimeraFila);
    	
    	//Estilo de las celdas de totales y su fuente
    	HSSFCellStyle estiloTotales = libroExcel.createCellStyle();
    	estiloTotales.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	estiloTotales.setBottomBorderColor((short) 8);
    	estiloTotales.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	estiloTotales.setLeftBorderColor((short) 8);
    	estiloTotales.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	estiloTotales.setRightBorderColor((short) 8);
    	estiloTotales.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	estiloTotales.setTopBorderColor((short) 8);
    	estiloTotales.setFillForegroundColor((new HSSFColor.GREY_25_PERCENT()).getIndex());
    	estiloTotales.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    	estiloTotales.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	HSSFFont fuenteTotales = libroExcel.createFont();
    	fuenteTotales.setFontHeightInPoints((short) 10);
    	fuenteTotales.setFontName(HSSFFont.FONT_ARIAL);
    	fuenteTotales.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    	estiloTotales.setFont(fuenteTotales);
    	
    	//Estilo de las celdas normales y su fuente
    	HSSFCellStyle estiloCelda = libroExcel.createCellStyle();
    	estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	estiloCelda.setBottomBorderColor((short) 8);
    	estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	estiloCelda.setLeftBorderColor((short) 8);
    	estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	estiloCelda.setRightBorderColor((short) 8);
    	estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	estiloCelda.setTopBorderColor((short) 8);
    	estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);    	
    	HSSFFont fuenteCelda = libroExcel.createFont();
    	fuenteCelda.setFontHeightInPoints((short) 8);
    	fuenteCelda.setFontName(HSSFFont.FONT_ARIAL);
    	estiloCelda.setFont(fuenteCelda);
    	
    	int i=0;
    	short j=0;
    	
    	HSSFRow filaOtrosDatos = hoja.createRow(i++);    
        HSSFCell celdaOtrosDatos = filaOtrosDatos.createCell(j++);
        celdaOtrosDatos.setCellValue(dato.getUsuario());    
        celdaOtrosDatos.setCellStyle(estiloTotales);
        celdaOtrosDatos = filaOtrosDatos.createCell(j++);
        celdaOtrosDatos.setCellValue(dato.getFecha());    
        celdaOtrosDatos.setCellStyle(estiloTotales);
        HSSFRow filaBlanca = hoja.createRow(i++); 
    	
    	//fILA DE TÍTULOS
    	HSSFRow filaTitulos = hoja.createRow(i++);
    	j=0;
        for (String valorCelda: uors) {
            HSSFCell celda = filaTitulos.createCell(j++);
            celda.setCellValue(valorCelda);    
            if (j == 0) celda.setCellStyle(estiloCeldaTitulo);
            else if (j == uors.size()) celda.setCellStyle(estiloTotales);
            else celda.setCellStyle(estiloPrimeraFila);
        }
    	
    	//rESTO DE FILAS
        for (Vector<String> filaInforme: filas) {
            HSSFRow filaDatos = hoja.createRow(i++);
            j = 0;
            for (String valorCelda: filaInforme) {
                HSSFCell celda = filaDatos.createCell(j++);
                celda.setCellValue(valorCelda);
                if (j-1 == 0) celda.setCellStyle(estiloCeldaTitulo);
                else if (j == filaInforme.size()) celda.setCellStyle(estiloTotales);
                else celda.setCellStyle(estiloCelda);
            }
        }
    
        String rutaArchivoSalida;
        try {
            File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
            File informe = File.createTempFile("estadistica_registro_", ".xls", directorioTemp);

            FileOutputStream archivoSalida = new FileOutputStream(informe);
            libroExcel.write(archivoSalida);
            archivoSalida.close();

            rutaArchivoSalida = informe.getName();

        } catch (IOException ioe) {
            throw new TechnicalException(ioe.getMessage(), ioe);
        }

        return rutaArchivoSalida;
        
    	
    	
    	
    }
    
    public String generarInformePDF(Collection datosInforme, UsuarioValueObject usuVO, 
    		String url, String[] paramsBD) throws TechnicalException {
    	
    	Vector<Vector<String>>  filas = new Vector<Vector<String>>();
		Vector<String> uors = new Vector<String>();
		DatoEstadisticaRegistro dato = new DatoEstadisticaRegistro();
		Vector<String> titulos = new Vector<String>();
		
		for (Object objDato : datosInforme) {
            dato = (DatoEstadisticaRegistro) objDato;            
		}
		filas = dato.getFilas();
		uors = dato.getUors();
		titulos.add(dato.getTitulo());
		titulos.add(dato.getUsuario());
		titulos.add(dato.getFecha());
		
		ParserInformeEstadisticasRegistro pier = new ParserInformeEstadisticasRegistro();	
		String fichero=pier.toXml(filas, uors, titulos);
		GeneralValueObject gVO = new GeneralValueObject();
		
		gVO.setAtributo("baseDir", m_Conf.getString("PDF.base_dir"));
		gVO.setAtributo("aplPathReal", url);
		gVO.setAtributo("usuDir",usuVO.getDtr());
		gVO.setAtributo("pdfFile", "InformeRegistro");

        int index = url.indexOf("\\");
        if (index > 0) url = url.substring(index);
		gVO.setAtributo("estilo", "css/informesPDF.css");
		gVO.setAtributo("tamPie", "20");
		gVO.setAtributo("pagina", "1");

		GeneralPDF pdf = new GeneralPDF(paramsBD, gVO);
		File pdfFile = pdf.transformaXML(fichero, "informeParticularPDF");
		Vector<File> ficheros = new Vector<File>();
		ficheros.add(pdfFile);

		return pdf.getPdf(ficheros)+".pdf";
    	
    }
    
    
    
    
    
    /**
     * Genera el documento XML que se parseará para generar el pdf.
     * A tener en cuenta:
     * 		Fichero XSL = InformeEstadisticasRegistro.xsl
     * 		Fichero CSS = informesPDF.css
     * */
    
    public String toXml(Vector<Vector<String>> filas, Vector<String> uors, Vector titulos){
	
	Vector<String> fila = new Vector<String>();
    StringBuffer textoXml = new StringBuffer("");
    textoXml.append ("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
    textoXml.append("<informe>");
    
    textoXml.append("<titulos>");
    for(int i=0;i<titulos.size();i++){
    	textoXml.append("<titulo><valor>" + titulos.get(i) + "</valor></titulo>");
    }
    textoXml.append("</titulos>");
    
    textoXml.append("<uors>");
    for (int i=0;i<uors.size();i++){
    	textoXml.append("<uor><nom>" + uors.get(i) + "</nom></uor>");
    }
    textoXml.append("</uors>");
    textoXml.append("<lineas>");
    
    for(int j=0;j<filas.size();j++){
    	fila = filas.get(j);
    	textoXml.append("<linea>");
    	for (int k=0;k<fila.size();k++){
    		textoXml.append("<contador><valor>" + fila.get(k) + 
    				"</valor></contador>");
    	}
    	textoXml.append("</linea>");
    }
    
    textoXml.append("</lineas>");
    textoXml.append("</informe>");

    return textoXml.toString();
    
    }
    
    
}