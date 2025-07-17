package es.altia.agora.business.registro.persistence;

import java.io.File;
import java.util.Vector;

import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.sqlxmlpdf.GeneralPDF;

public class ImpresionCuneusPDFManager extends ImpresionCuneusManager {
	  
	protected static Config m_Conf = ConfigServiceHelper.getConfig("common");
	private String TIPO_FICHERO = "pdf";

	protected ImpresionCuneusPDFManager() {}
	
	@Override
	public String imprimirCuneus(RegistroValueObject elRegistroESVO, UsuarioValueObject usuVO, 
			String posicionSello, String idiomaSello, String nCopiasSello, String sUrl, String realPath) {
		
        GeneralValueObject gVO = new GeneralValueObject();
        Vector<File> ficheros = new Vector<File>();
        String plantilla = "cuneusRegistro";
        String sObservaciones = elRegistroESVO.getObservaciones();
        sObservaciones = AdaptadorSQLBD.js_unescape(sObservaciones);
        gVO.setAtributo("baseDir", m_Conf.getString("PDF.base_dir"));
        gVO.setAtributo("aplPathReal", realPath);
        gVO.setAtributo("usuDir", usuVO.getDtr());
        gVO.setAtributo("pdfFile", "registro");
        String estilo = "css/informeCuneus.css";
        gVO.setAtributo("estilo", estilo);
        GeneralPDF pdf = new GeneralPDF(usuVO.getParamsCon(), gVO);

        // Obtenemos el codigo visible de la UOR a partir de su codigo interno.
        Vector unidades = UORsManager.getInstance().getListaUORsPorCodigo(usuVO.getUnidadOrgCod(), usuVO.getParamsCon());
        UORDTO unidad = (UORDTO)unidades.get(0);
        long numReg = elRegistroESVO.getNumReg();
        String textoXML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><CUNEUS>";
        String contenidoXML = "<COPIA>"
                + "<POSICION_CUNEUS>" + posicionSello + "</POSICION_CUNEUS>"
                + "<NOMBREOFICINA>"+ elRegistroESVO.getNombreOficinaRegistro()+ "</NOMBREOFICINA>"
                + "<REGISTRO>" + usuVO.getUnidadOrg() + "</REGISTRO>"
                + "<CODREGISTRO>" + unidad.getUor_cod_vis() + "</CODREGISTRO>"
                + "<TIPO_REGISTRO>" + elRegistroESVO.getTipoReg() + "</TIPO_REGISTRO>"
                + "<AYTO>" + usuVO.getEnt() + "</AYTO><EJERCICIO>" + elRegistroESVO.getAnoReg() + "</EJERCICIO>"
                + "<NUMERO>" + numReg + "</NUMERO>"
                + "<FECHA>" + elRegistroESVO.getFecEntrada() + "</FECHA>"
                + "<IDIOMA>" + idiomaSello + "</IDIOMA>"
                + "<ESCUDO>" + usuVO.getOrgIco().substring(1) + "</ESCUDO>"
                + "<OBSERVACIONES>" + (sObservaciones != null ? sObservaciones.trim() : "") + "</OBSERVACIONES>"
                + "</COPIA>";
        String finXML = "</CUNEUS>";

        for (int i = 0; i < Integer.parseInt(nCopiasSello); i++) {
            textoXML += contenidoXML;
            if (i != Integer.parseInt(nCopiasSello) - 1) textoXML += pdf.construyeTablaVacia("saltoPagina");
        }
        textoXML += finXML;
        if (m_Log.isDebugEnabled()) m_Log.debug("MantAnotacionRegistroAction. imprimirCuneus. " + textoXML);
        File f = pdf.transformaXML(textoXML, plantilla);

        String nombreFichero = null;
        if (f != null) {
            ficheros.add(f);
            nombreFichero = pdf.getPdf(ficheros);
        }
        return nombreFichero;
	}

	// Para informar al action de que tipo de fichero se ha generado
	public String tipoFichero(){
		return TIPO_FICHERO;
	}
}
