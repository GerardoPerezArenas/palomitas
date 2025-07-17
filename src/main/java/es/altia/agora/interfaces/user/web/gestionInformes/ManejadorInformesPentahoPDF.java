package es.altia.agora.interfaces.user.web.gestionInformes;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.pentaho.reporting.engine.classic.core.modules.parser.base.ReportGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.gestionInformes.InformeValueObject;
import es.altia.agora.business.gestionInformes.CabInformeValueObject;
import es.altia.agora.business.gestionInformes.CabPaginaInformeValueObject;
import es.altia.agora.business.gestionInformes.PiePaginaInformeValueObject;
import es.altia.agora.business.gestionInformes.PieInformeValueObject;
import es.altia.agora.business.gestionInformes.CuerpoInformeValueObject;
import es.altia.agora.business.gestionInformes.InformeTableModel;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.modules.gui.base.PreviewDialog;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;

public class ManejadorInformesPentahoPDF {

    protected static Log m_Log =
            LogFactory.getLog(ManejadorInformesPentahoPDF.class.getName());
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");
    private static ManejadorInformesPentahoPDF instance = null;


    public static ManejadorInformesPentahoPDF getInstance() {
     //si no hay ninguna instancia de esta clase tenemos que crear una.
     if (instance == null) {
        // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
        synchronized(ManejadorInformesPentahoPDF.class){
         if (instance == null)
            instance = new ManejadorInformesPentahoPDF();
        }
     }
     return instance;
    }

   
    public String buscarTipoPorDesc(Vector listaTiposCamposInforme, String descripcion) {

    String tipo = "";

    try {
        m_Log.debug("--------------> EN buscarTipoPorDesc "+descripcion);
        int j=0;
        boolean encontrado = false;

        while (!encontrado && j<listaTiposCamposInforme.size()) {
            if (((String)((HashMap)listaTiposCamposInforme.get(j)).get("descripcion")).equalsIgnoreCase(descripcion)) {
                tipo = (String)((HashMap)listaTiposCamposInforme.get(j)).get("tipo");
                encontrado = true;
            }
            j++;
        }
        m_Log.debug("--------------> buscarTipoPorDesc DEVUELVE "+tipo);

    } catch (Exception e) {
        e.printStackTrace();
    }
        return tipo;
    }

    public int buscarPosCampoCabInforme(Vector listaTiposCamposInforme, InformeValueObject informeVO, String descripcion) {

    boolean encontrado = false;
    int i=0;

    try {
        m_Log.debug("--------------> EN buscarPosCampoCabInforme "+descripcion);
        Vector vectorCabInformeVO = informeVO.getVectorCabInformeVO();
        CabInformeValueObject cabInformeVO;
        String tipo = buscarTipoPorDesc(listaTiposCamposInforme,descripcion);
        m_Log.debug("EN BUSCAR CAMPO CABECERA INFORME");
        m_Log.debug("DESCRIPCION " + descripcion);
        m_Log.debug("TIPO " + tipo);

        while (!encontrado && i<vectorCabInformeVO.size()) {
            cabInformeVO = (CabInformeValueObject) vectorCabInformeVO.get(i);
            if (cabInformeVO.getTipo().equals(tipo)) {
                encontrado = true;
            } else {
                i++;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        if (encontrado) {
            m_Log.debug("EN BUSCAR POSICION CAMPO CABECERA INFORME DEVUELVE "+i);
            return i;
        } else {
            m_Log.debug("EN BUSCAR POSICION CAMPO CABECERA INFORME DEVUELVE -1");
            return -1;
        }
    }

    public int buscarPosCampoCabPaginaInforme(Vector listaTiposCamposInforme, InformeValueObject informeVO, String descripcion) {

    boolean encontrado = false;
    int i=0;

    try {
        m_Log.debug("--------------> EN buscarPosCampoCabPaginaInforme "+descripcion);
        Vector vectorCabPaginaInformeVO = informeVO.getVectorCabPaginaInformeVO();
        CabPaginaInformeValueObject cabPaginaInformeVO;
        String tipo = buscarTipoPorDesc(listaTiposCamposInforme,descripcion);
        m_Log.debug("EN BUSCAR CAMPO CABECERA PAGINA INFORME");
        m_Log.debug("DESCRIPCION " + descripcion);
        m_Log.debug("TIPO " + tipo);

        while (!encontrado && i<vectorCabPaginaInformeVO.size()) {
            cabPaginaInformeVO = (CabPaginaInformeValueObject) vectorCabPaginaInformeVO.get(i);
            if (cabPaginaInformeVO.getTipo().equals(tipo)) {
                encontrado = true;
            } else {
                i++;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        if (encontrado) {
            m_Log.debug("EN BUSCAR POSICION CAMPO CABECERA INFORME DEVUELVE "+i);
            return i;
        } else {
            m_Log.debug("EN BUSCAR POSICION CAMPO CABECERA INFORME DEVUELVE -1");
            return -1;
        }
    }

    public int buscarPosCampoPiePaginaInforme(Vector listaTiposCamposInforme, InformeValueObject informeVO, String descripcion) {

    boolean encontrado = false;
    int i=0;

    try {
        m_Log.debug("--------------> EN buscarPosCampoPiePaginaInforme "+descripcion);
        Vector vectorPiePaginaInformeVO = informeVO.getVectorPiePaginaInformeVO();
        PiePaginaInformeValueObject cabPiePaginaInformeVO;
        String tipo = buscarTipoPorDesc(listaTiposCamposInforme,descripcion);
        m_Log.debug("EN BUSCAR CAMPO PIE PAGINA INFORME");
        m_Log.debug("DESCRIPCION " + descripcion);
        m_Log.debug("TIPO " + tipo);

        while (!encontrado && i<vectorPiePaginaInformeVO.size()) {
            cabPiePaginaInformeVO = (PiePaginaInformeValueObject) vectorPiePaginaInformeVO.get(i);
            if (cabPiePaginaInformeVO.getTipo().equals(tipo)) {
                encontrado = true;
            } else {
                i++;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        if (encontrado) {
            m_Log.debug("EN BUSCAR POSICION CAMPO CABECERA INFORME DEVUELVE "+i);
            return i;
        } else {
            m_Log.debug("EN BUSCAR POSICION CAMPO CABECERA INFORME DEVUELVE -1");
            return -1;
        }
    }

    public int buscarPosCampoPieInforme(Vector listaTiposCamposInforme, InformeValueObject informeVO, String descripcion) {

    boolean encontrado = false;
    int i=0;

    try {
        m_Log.debug("--------------> EN buscarPosCampoPieInforme "+descripcion);
        Vector vectorPiInformeVO = informeVO.getVectorPieInformeVO();
        PieInformeValueObject pieInformeVO;
        String tipo = buscarTipoPorDesc(listaTiposCamposInforme,descripcion);
        m_Log.debug("EN BUSCAR CAMPO PIE INFORME");
        m_Log.debug("DESCRIPCION " + descripcion);
        m_Log.debug("TIPO " + tipo);

        while (!encontrado && i<vectorPiInformeVO.size()) {
            pieInformeVO = (PieInformeValueObject) vectorPiInformeVO.get(i);
            if (pieInformeVO.getTipo().equals(tipo)) {
                encontrado = true;
            } else {
                i++;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        if (encontrado) {
            m_Log.debug("EN BUSCAR POSICION CAMPO CABECERA INFORME DEVUELVE "+i);
            return i;
        } else {
            m_Log.debug("EN BUSCAR POSICION CAMPO CABECERA INFORME DEVUELVE -1");
            return -1;
        }
    }

    public Element createReportHeaderElement(String urlLogo, String entidad, Vector listaTiposCamposInforme, InformeValueObject informeVO, Document dom) {

        Element reportheaderElement = dom.createElement("reportheader");

        try {
            boolean hayCabecera = (informeVO.getVectorCabInformeVO().size() > 0);

            m_Log.debug("hayCabeceraInforme " + hayCabecera);
            int posLogo = buscarPosCampoCabInforme(listaTiposCamposInforme, informeVO, "LOGO");
            int posNombreEntidad = buscarPosCampoCabInforme(listaTiposCamposInforme, informeVO, "NOMBRE_ENTIDAD");
            int posTitulo = buscarPosCampoCabInforme(listaTiposCamposInforme, informeVO, "TITULO_INFORME");
            int posCriterios = buscarPosCampoCabInforme(listaTiposCamposInforme, informeVO, "CRITERIOS_INFORME");
            int posFecha = buscarPosCampoCabInforme(listaTiposCamposInforme, informeVO, "FECHA_INFORME");
            int posNumPag = buscarPosCampoCabInforme(listaTiposCamposInforme, informeVO, "NUM_PAGINA_INFORME");
            int posTotalPag = buscarPosCampoCabInforme(listaTiposCamposInforme, informeVO, "TOTAL_PAGINAS_INFORME");

            reportheaderElement.setAttribute("vertical-alignment", "middle");
            if (hayCabecera) {
                reportheaderElement.setAttribute("height", "120");
            } else {
                reportheaderElement.setAttribute("height", "30");
            }
            reportheaderElement.setAttribute("fontstyle", "plain");
            reportheaderElement.setAttribute("fontname", "SansSerif");
            reportheaderElement.setAttribute("fontsize", "11");
            reportheaderElement.setAttribute("onfirstpage", "true");
            reportheaderElement.setAttribute("fsbold", "true");
            reportheaderElement.setAttribute("alignment", "left");
            reportheaderElement.setAttribute("reserve-literal", "...");

            Element labelElement, lineElement;
            Text labelText;

            if (posLogo != -1) {  // Hay logo
                Element logo = dom.createElement("imageref");
                //logo.setAttribute("width", "25%");
                //logo.setAttribute("height", "50%");
                logo.setAttribute("width", "30%");
                logo.setAttribute("height", "60%");
                logo.setAttribute("src", urlLogo);
                reportheaderElement.appendChild(logo);
            }
            if (posTitulo != -1) {   // Hay titulo
                labelElement = dom.createElement("label");
                if (posLogo != -1) {
                    labelElement.setAttribute("x", "26%");
                }
                labelElement.setAttribute("width", "54%");
                labelElement.setAttribute("height", "10%");
                labelText = dom.createTextNode(informeVO.getTitulo());
                labelElement.appendChild(labelText);
                reportheaderElement.appendChild(labelElement);
            }
            if (posNombreEntidad != -1) { // Hay nombre entidad
                labelElement = dom.createElement("label");
                if (posLogo != -1) {
                    labelElement.setAttribute("x", "26%");
                }
                labelElement.setAttribute("y", "10%");
                labelElement.setAttribute("width", "44%");
                labelElement.setAttribute("height", "10%");
                labelElement.setAttribute("fontsize", "7");
                labelText = dom.createTextNode(entidad);
                labelElement.appendChild(labelText);
                reportheaderElement.appendChild(labelElement);
            }
            int posY = 20;
            int j = 10;
            if (posCriterios != -1) {   // Hay criterios
                for (int i = 0; i < informeVO.getCriterios().size(); i++) {
                    labelElement = dom.createElement("label");
                    if (posLogo != -1) {
                        labelElement.setAttribute("x", "26%");
                    }
                    labelElement.setAttribute("y", posY + "%");
                    labelElement.setAttribute("width", "44%");
                    labelElement.setAttribute("height", "10%");
                    labelElement.setAttribute("fontsize", "8");
                    labelText = dom.createTextNode((String) informeVO.getCriterios().get(i));
                    labelElement.appendChild(labelText);
                    reportheaderElement.appendChild(labelElement);
                    posY = posY + j;
                }
            }
            
            if (posFecha != -1) {
                Element datefieldElement = dom.createElement("date-field");
                //datefieldElement.setAttribute("x", "90%");
                //datefieldElement.setAttribute("width", "50%");
                datefieldElement.setAttribute("x", "80%");
                datefieldElement.setAttribute("width", "25%");
                datefieldElement.setAttribute("height", "15%");
                datefieldElement.setAttribute("fontsize", "9");
                datefieldElement.setAttribute("alignment", "right");
                datefieldElement.setAttribute("format", "d-MMM-yyyy");
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");

                datefieldElement.setAttribute("fieldname", "report.date");
                reportheaderElement.appendChild(datefieldElement);
            }

            if (posNumPag != -1) {
                Element stringFieldElement = dom.createElement("string-field");
                stringFieldElement.setAttribute("x", "90%");
                stringFieldElement.setAttribute("y", "20%");
                stringFieldElement.setAttribute("width", "50%");
                stringFieldElement.setAttribute("height", "10%");
                stringFieldElement.setAttribute("fontsize", "8");
                stringFieldElement.setAttribute("alignment", "right");
                stringFieldElement.setAttribute("fieldname", "pageX");
                reportheaderElement.appendChild(stringFieldElement);
            }

            if (posTotalPag != -1) {
                Element stringFieldElement = dom.createElement("string-field");
                stringFieldElement.setAttribute("x", "70%");
                stringFieldElement.setAttribute("y", "35%");
                stringFieldElement.setAttribute("width", "50%");
                stringFieldElement.setAttribute("height", "10%");
                stringFieldElement.setAttribute("fontsize", "8");
                stringFieldElement.setAttribute("alignment", "right");
                stringFieldElement.setAttribute("fieldname", "TotalPage");
                reportheaderElement.appendChild(stringFieldElement);
            }

            if (hayCabecera) {
                lineElement = dom.createElement("line");
                lineElement.setAttribute("x1", "0");
                lineElement.setAttribute("y1", "60%");
                lineElement.setAttribute("x2", "100%");
                lineElement.setAttribute("y2", "60%");
                lineElement.setAttribute("weight", "0.5");
                reportheaderElement.appendChild(lineElement);
                insertarNombresColumnas(reportheaderElement, informeVO, dom, hayCabecera);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportheaderElement;
    }


    private void insertarNombresColumnas(Element reportheaderElement, InformeValueObject informeVO, Document dom, boolean hayCabecera) {
        Element labelElement, lineElement;
        Text labelText;
        //boolean hayCabecera = (informeVO.getVectorCabInformeVO().size() > 0);

        Vector vectorCuerpoInformeVO = informeVO.getVectorCuerpoInformeVO();
        CuerpoInformeValueObject cuerpoInformeVO;
        int posX = 0;
        for (int i = 0; i < vectorCuerpoInformeVO.size(); i++) {
            cuerpoInformeVO = (CuerpoInformeValueObject) vectorCuerpoInformeVO.elementAt(i);
            labelElement = dom.createElement("label");
            labelElement.setAttribute("x", String.valueOf(posX).concat("%"));
            if (hayCabecera) {
                labelElement.setAttribute("y", "65%");
                labelElement.setAttribute("height", "25%");
            } else {
                labelElement.setAttribute("height", "80%");
            }
            labelElement.setAttribute("width", cuerpoInformeVO.getAncho().concat("%"));
            labelElement.setAttribute("alignment", cuerpoInformeVO.getAlign().toLowerCase());

            labelText = dom.createTextNode(cuerpoInformeVO.getTitulo());
            labelElement.appendChild(labelText);
            reportheaderElement.appendChild(labelElement);
            posX = posX + Integer.valueOf(cuerpoInformeVO.getAncho()).intValue();
        }

        lineElement = dom.createElement("line");
        lineElement.setAttribute("x1", "0");
        lineElement.setAttribute("y1", "90%");
        lineElement.setAttribute("x2", "100%");
        lineElement.setAttribute("y2", "90%");
        lineElement.setAttribute("weight", "0.5");
        lineElement.setAttribute("color", "#CFCFCF");
        reportheaderElement.appendChild(lineElement);
    }



    public Element createPageHeaderElement(String urlLogo, String entidad, Vector listaTiposCamposInforme, InformeValueObject informeVO,Document dom){

        Element pageheaderElement = dom.createElement("pageheader");
        try {

            boolean hayCabecera = (informeVO.getVectorCabPaginaInformeVO().size()>0);
            m_Log.debug("hayCabeceraPaginaInforme " + hayCabecera);
            int posLogo =  buscarPosCampoCabPaginaInforme(listaTiposCamposInforme,informeVO,"LOGO");
            int posNombreEntidad =  buscarPosCampoCabPaginaInforme(listaTiposCamposInforme,informeVO,"NOMBRE_ENTIDAD");
            int posTitulo =  buscarPosCampoCabPaginaInforme(listaTiposCamposInforme,informeVO,"TITULO_INFORME");
            int posCriterios =  buscarPosCampoCabPaginaInforme(listaTiposCamposInforme,informeVO,"CRITERIOS_INFORME");
            int posFecha =  buscarPosCampoCabPaginaInforme(listaTiposCamposInforme,informeVO,"FECHA_INFORME");
            int posNumPag =  buscarPosCampoCabPaginaInforme(listaTiposCamposInforme,informeVO,"NUM_PAGINA_INFORME");
            int posTotalPag =  buscarPosCampoCabPaginaInforme(listaTiposCamposInforme,informeVO,"TOTAL_PAGINAS_INFORME");

            pageheaderElement.setAttribute("vertical-alignment", "middle");
            if (hayCabecera) {
                pageheaderElement.setAttribute("height", "120");
            } else {
                pageheaderElement.setAttribute("height", "30");
            }
            pageheaderElement.setAttribute("fontstyle", "plain");
            pageheaderElement.setAttribute("fontname", "SansSerif");
            pageheaderElement.setAttribute("fontsize", "11");

            // Si hay cabecera de informe entonces no mostraremos en la primera pagina la cabecera de pagina
            boolean hayCabeceraInforme = (informeVO.getVectorCabInformeVO().size()>0);
             m_Log.debug(" -- hayCabeceraInforme en createPageHeaderElement : " + hayCabeceraInforme);
            if (hayCabeceraInforme){
                pageheaderElement.setAttribute("onfirstpage", "false");
            }else pageheaderElement.setAttribute("onfirstpage", "true");

            pageheaderElement.setAttribute("fsbold", "true");
            pageheaderElement.setAttribute("alignment", "left");
            pageheaderElement.setAttribute("reserve-literal","...");

            Element labelElement, lineElement;
            Text labelText;

            if (posLogo!=-1) {  // Hay logo
                Element logo = dom.createElement("imageref");
                //logo.setAttribute("width","25%");
                //logo.setAttribute("height","50%");
                logo.setAttribute("width", "30%");
                logo.setAttribute("height", "60%");
                logo.setAttribute("src",urlLogo);
                pageheaderElement.appendChild(logo);
            }
            if (posTitulo!=-1) {   // Hay titulo
                labelElement = dom.createElement("label");
                if (posLogo!=-1) {
                    labelElement.setAttribute("x", "26%");
                }
                labelElement.setAttribute("width", "54%");
                labelElement.setAttribute("height", "10%");
                labelText = dom.createTextNode(informeVO.getTitulo());
                labelElement.appendChild(labelText);
                pageheaderElement.appendChild(labelElement);
            }
            if (posNombreEntidad!=-1) { // Hay nombre entidad
                labelElement = dom.createElement("label");
                if (posLogo!=-1) {
                    labelElement.setAttribute("x", "26%");
                }
                labelElement.setAttribute("y", "10%");
                labelElement.setAttribute("width", "44%");
                labelElement.setAttribute("height", "10%");
                labelElement.setAttribute("fontsize", "7");
                labelText = dom.createTextNode(entidad);
                labelElement.appendChild(labelText);
                pageheaderElement.appendChild(labelElement);
            }
            int posY=20;
            int j=10;
            if (posCriterios!=-1) {   // Hay criterios
                    for (int i=0;i<informeVO.getCriterios().size();i++) {
                    labelElement = dom.createElement("label");
                    if (posLogo!=-1) {
                        labelElement.setAttribute("x", "26%");
                    }
                    labelElement.setAttribute("y", posY+"%");
                    labelElement.setAttribute("width", "44%");
                    labelElement.setAttribute("height", "10%");
                    labelElement.setAttribute("fontsize", "8");
                    labelText = dom.createTextNode((String)informeVO.getCriterios().get(i));
                    labelElement.appendChild(labelText);
                    pageheaderElement.appendChild(labelElement);
                    posY = posY + j;
                }
            }
            
            if (posFecha!=-1) {
                Element datefieldElement = dom.createElement("date-field");
                datefieldElement.setAttribute("x", "80%");
                datefieldElement.setAttribute("width", "25%");
                datefieldElement.setAttribute("height", "15%");
                datefieldElement.setAttribute("fontsize", "9");
                datefieldElement.setAttribute("alignment", "right");
                datefieldElement.setAttribute("format", "d-MMM-yyyy");
                datefieldElement.setAttribute("fieldname", "report.date");
                pageheaderElement.appendChild(datefieldElement);
            }

            if (posNumPag!=-1) {
                Element stringFieldElement = dom.createElement("string-field");
                stringFieldElement.setAttribute("x", "90%");
                stringFieldElement.setAttribute("y", "20%");
                stringFieldElement.setAttribute("width", "50%");
                stringFieldElement.setAttribute("height", "10%");
                stringFieldElement.setAttribute("fontsize", "8");
                stringFieldElement.setAttribute("alignment", "right");
                stringFieldElement.setAttribute("fieldname", "pageX");
                pageheaderElement.appendChild(stringFieldElement);
            }

            if (posTotalPag!=-1) {
                Element stringFieldElement = dom.createElement("string-field");
                stringFieldElement.setAttribute("x", "70%");
                stringFieldElement.setAttribute("y", "35%");
                stringFieldElement.setAttribute("width", "50%");
                stringFieldElement.setAttribute("height", "10%");
                stringFieldElement.setAttribute("fontsize", "8");
                stringFieldElement.setAttribute("alignment", "right");
                stringFieldElement.setAttribute("fieldname", "TotalPage");
                pageheaderElement.appendChild(stringFieldElement);
            }

            if (hayCabecera) {
                lineElement = dom.createElement("line");
                lineElement.setAttribute("x1", "0");
                lineElement.setAttribute("y1", "60%");
                lineElement.setAttribute("x2", "100%");
                lineElement.setAttribute("y2", "60%");
                lineElement.setAttribute("weight", "0.5");
                pageheaderElement.appendChild(lineElement);

            }

            insertarNombresColumnas(pageheaderElement, informeVO, dom, hayCabecera);

        } catch(Exception e) {
            e.printStackTrace();
        }
        return pageheaderElement;
    }


    public Element createItemsElement(InformeValueObject informeVO, InformeTableModel informeTableModel, Document dom){

        Element itemsElement = dom.createElement("items");

    try {
        Element bandElement = dom.createElement("band");
        bandElement.setAttribute("vertical-alignment", "middle");
        bandElement.setAttribute("name", "dato");
        bandElement.setAttribute("fsbold", "false");
        bandElement.setAttribute("fontsize", "10");
        bandElement.setAttribute("fontstyle", "plain");
        bandElement.setAttribute("fontname", "Arial");
        itemsElement.appendChild(bandElement);

        Vector vectorCuerpoInformeVO = informeVO.getVectorCuerpoInformeVO();
        CuerpoInformeValueObject cuerpoInformeVO;
        int posX = 0;
        Element stringfieldElement;

        m_Log.debug("EN CREATE ITEMS ELEMEMT");
        m_Log.debug("vectorCuerpoInformeVO.size() "+vectorCuerpoInformeVO.size());
        m_Log.debug("informeTableModel.getColumnNames().size() "+informeTableModel.getColumnNames().size());
        m_Log.debug("informeTableModel.getDATA().size() "+informeTableModel.getDATA().size());

        for (int i=0;i<vectorCuerpoInformeVO.size();i++) {
            cuerpoInformeVO = (CuerpoInformeValueObject) vectorCuerpoInformeVO.elementAt(i);
            stringfieldElement = dom.createElement("string-field");
            stringfieldElement.setAttribute("x", String.valueOf(posX).concat("%"));
            stringfieldElement.setAttribute("width", cuerpoInformeVO.getAncho().concat("%"));
            stringfieldElement.setAttribute("height", "12");
            stringfieldElement.setAttribute("alignment", cuerpoInformeVO.getAlign().toLowerCase());
            stringfieldElement.setAttribute("fieldname", (String)informeTableModel.getColumnNames().get(i));
            stringfieldElement.setAttribute("dynamic",String.valueOf(cuerpoInformeVO.getElipsis()));
            itemsElement.appendChild(stringfieldElement);
            posX = posX + Integer.valueOf(cuerpoInformeVO.getAncho()).intValue();
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
        return itemsElement;

    }


    public Element createPageFooterElement(Vector listaTiposCamposInforme, InformeValueObject informeVO, Document dom) {

    Element pagefooterElement = dom.createElement("pagefooter");

    try {

        int posNumPag =  buscarPosCampoPiePaginaInforme(listaTiposCamposInforme,informeVO,"NUM_PAGINA_INFORME");
        int posNumTotalPag =  buscarPosCampoPiePaginaInforme(listaTiposCamposInforme,informeVO,"NUM_DE_TOTAL_PAG_INFORME");

        pagefooterElement.setAttribute("height","20");
        pagefooterElement.setAttribute("alignment","right");
        pagefooterElement.setAttribute("vertical-alignment","middle");
        pagefooterElement.setAttribute("fontname","Arial");
        pagefooterElement.setAttribute("fontsize","9");
        pagefooterElement.setAttribute("fontstyle","plain");

        Element lineElement = dom.createElement("line");
        lineElement.setAttribute("x1", "0");
        lineElement.setAttribute("y1", "0");
        lineElement.setAttribute("x2", "100%");
        lineElement.setAttribute("y2", "0");
        lineElement.setAttribute("height", "30%");
        lineElement.setAttribute("weight", "0.5");
        pagefooterElement.appendChild(lineElement);

        if (posNumPag!=-1) {
            Element stringfieldElement = dom.createElement("string-field");
            if (posNumTotalPag!=-1) { // Van los dos
                stringfieldElement.setAttribute("alignment","left");
            }
            stringfieldElement.setAttribute("y", "30%");
            stringfieldElement.setAttribute("width", "100%");
            stringfieldElement.setAttribute("height", "70%");
            stringfieldElement.setAttribute("fieldname", "pageX");
            pagefooterElement.appendChild(stringfieldElement);
        }

        
        if (posNumTotalPag!=-1) {
            Element stringfieldElement = dom.createElement("string-field");
            if (posNumPag!=-1) { // Van los dos                
                stringfieldElement.setAttribute("x", "80%");
            }
            stringfieldElement.setAttribute("y", "30%");            
            stringfieldElement.setAttribute("width", "20%");
            stringfieldElement.setAttribute("height", "70%");
            stringfieldElement.setAttribute("fieldname", "pageXofY");
            pagefooterElement.appendChild(stringfieldElement);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        return pagefooterElement;
    }

    public Element createReportFooterElement(Vector listaTiposCamposInforme, InformeValueObject informeVO, Document dom) {

    Element reportfooterElement = dom.createElement("reportfooter");

    try {

        int posTotalPag =  buscarPosCampoPieInforme(listaTiposCamposInforme,informeVO,"TOTAL_PAGINAS_INFORME");
        int posFirmado =  buscarPosCampoPieInforme(listaTiposCamposInforme,informeVO,"FIRMADO_POR");

        reportfooterElement.setAttribute("alignment","right");
        reportfooterElement.setAttribute("vertical-alignment","middle");
        reportfooterElement.setAttribute("fontname","Arial");
        reportfooterElement.setAttribute("fontsize","9");
        reportfooterElement.setAttribute("fontstyle","plain");

        if (posTotalPag!=-1) {
            Element stringfieldElement = dom.createElement("string-field");
            stringfieldElement.setAttribute("width", "100%");
            stringfieldElement.setAttribute("height", "20");
            stringfieldElement.setAttribute("fieldname", "TotalPage");
            reportfooterElement.appendChild(stringfieldElement);
        }

        if (posFirmado!=-1) {
            Element lineElement = dom.createElement("line");
            lineElement.setAttribute("x1", "0");
            lineElement.setAttribute("y1", "15");
            lineElement.setAttribute("x2", "100%");
            lineElement.setAttribute("y2", "15");
            lineElement.setAttribute("weight", "0.5");
            lineElement.setAttribute("color", "#CFCFCF");
            reportfooterElement.appendChild(lineElement);

            Element labelElement = dom.createElement("label");
            labelElement.setAttribute("alignment", "left");
            labelElement.setAttribute("x", "5%");
            labelElement.setAttribute("width", "50%");
            labelElement.setAttribute("height", "50");
            labelElement.setAttribute("fontsize", "10");
            Text labelText = dom.createTextNode("Recibín:");
            labelElement.appendChild(labelText);
            reportfooterElement.appendChild(labelElement);

            labelElement = dom.createElement("label");
            labelElement.setAttribute("alignment", "left");
            labelElement.setAttribute("x", "5%");
            labelElement.setAttribute("width", "50%");
            labelElement.setAttribute("height", "90");
            labelElement.setAttribute("fontsize", "10");
            labelText = dom.createTextNode("En ____________________, a ___ de _______________ de ______");
            labelElement.appendChild(labelText);
            reportfooterElement.appendChild(labelElement);

            labelElement = dom.createElement("label");
            labelElement.setAttribute("x", "5%");
            labelElement.setAttribute("alignment", "left");
            labelElement.setAttribute("width", "50%");
            labelElement.setAttribute("height", "130");
            labelElement.setAttribute("fontsize", "10");
            labelText = dom.createTextNode("Asdo:");
            labelElement.appendChild(labelText);
            reportfooterElement.appendChild(labelElement);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return reportfooterElement;
    }


    public Element createFunctionsElement(Document dom) {

        Element functionsElement = dom.createElement("functions");

    try {

        Element propertyrefElement = dom.createElement("property-ref");
        propertyrefElement.setAttribute("name", "report.date");
        functionsElement.appendChild(propertyrefElement);

        Element functionElement = dom.createElement("function");
        // original
        functionElement.setAttribute("class", "org.jfree.report.function.PageOfPagesFunction");
        functionElement.setAttribute("name", "pageXofY");
        Element propertiesElement = dom.createElement("properties");
        Element propertyElement = dom.createElement("property");
        propertyElement.setAttribute("name", "format");
        Text propertyText = dom.createTextNode("Pag. {0}/{1}");
        propertyElement.appendChild(propertyText);
        propertiesElement.appendChild(propertyElement);
        functionElement.appendChild(propertiesElement);
        functionsElement.appendChild(functionElement);



        functionElement = dom.createElement("function");
        // original
        functionElement.setAttribute("class", "org.jfree.report.function.PageOfPagesFunction");
        functionElement.setAttribute("name", "pageX");

        propertiesElement = dom.createElement("properties");
        propertyElement = dom.createElement("property");
        propertyElement.setAttribute("name", "format");
        propertyText = dom.createTextNode("Pag. {0}");
        propertyElement.appendChild(propertyText);
        propertiesElement.appendChild(propertyElement);
        functionElement.appendChild(propertiesElement);
        functionsElement.appendChild(functionElement);


        functionElement = dom.createElement("function");
        functionElement.setAttribute("class", "org.jfree.report.function.PageOfPagesFunction");
        functionElement.setAttribute("name", "TotalPage");
        propertiesElement = dom.createElement("properties");
        propertyElement = dom.createElement("property");
        propertyElement.setAttribute("name", "format");
        propertyText = dom.createTextNode("Total de páginas: {1}");
        propertyElement.appendChild(propertyText);
        propertiesElement.appendChild(propertyElement);
        functionElement.appendChild(propertiesElement);
        functionsElement.appendChild(functionElement);


    } catch (Exception e){
        e.printStackTrace();
    }

        return functionsElement;

    }


    public String generarInfome(String host, String context, UsuarioValueObject usuario, int formato, Vector listaTiposCamposInforme, InformeValueObject informeVO, InformeTableModel informeTableModel, String accion, String protocolo) {

        String destinoInforme = null;
        try {

            String urlLogo = protocolo + "://" + host + context + usuario.getOrgIco();
            String entidad = usuario.getEnt();
            m_Log.debug("URLLOGO: " + urlLogo);
            m_Log.debug("ENTIDAD: " + entidad);

            //get an instance of factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //get an instance of builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //create an instance of DOM
            Document dom = db.newDocument();
            //create the root element
            Element reportElement = dom.createElement("report");
            dom.appendChild(reportElement);
            reportElement.setAttribute("name", "Informe Report");
            reportElement.setAttribute("pageformat", informeVO.getPapel());
            if (informeVO.getOrientacion().equalsIgnoreCase("horizontal")) {
                reportElement.setAttribute("orientation", "landscape");
            } else {
                reportElement.setAttribute("orientation", "portrait");
            }
            reportElement.setAttribute("topmargin", informeVO.getMargenSuperior());
            reportElement.setAttribute("bottommargin", informeVO.getMargenInferior());
            reportElement.setAttribute("leftmargin", informeVO.getMargenIzquierdo());
            reportElement.setAttribute("rightmargin", informeVO.getMargenDerecho());

            Element configurationElement = dom.createElement("configuration");
            Element propertyElement = dom.createElement("property");
            Text textElement = dom.createTextNode(";");

            propertyElement.setAttribute("name","org.jfree.report.modules.output.table.csv.Separator");
            propertyElement.appendChild(textElement);

            configurationElement.appendChild(propertyElement);

            reportElement.appendChild(configurationElement);


            if (!(formato==3)) {
                    m_Log.debug("EL INFORME TIENE CABECERA DE INFORME");
                    reportElement.appendChild(createReportHeaderElement(urlLogo,entidad,listaTiposCamposInforme,informeVO,dom));
                }
            if (formato==0) {
                    m_Log.debug("EL INFORME TIENE CABECERA DE PAGINA");
                    reportElement.appendChild(createPageHeaderElement(urlLogo,entidad,listaTiposCamposInforme,informeVO,dom));
                }
            if (informeVO.getVectorCuerpoInformeVO().size()!=0) {
                m_Log.debug("HAY DATOS EN CUERPO DEL INFORME");
                reportElement.appendChild(createItemsElement(informeVO,informeTableModel,dom));
            }
            if (!(formato==3)) {
                if (informeVO.getVectorPiePaginaInformeVO().size()!=0) {
                    m_Log.debug("EL INFORME TIENE PIE DE PAGINA");
                    reportElement.appendChild(createPageFooterElement(listaTiposCamposInforme,informeVO,dom));
                }
                if (informeVO.getVectorPieInformeVO().size()!=0) {
                    m_Log.debug("EL INFORME TIENE PIE DE INFORME");
                    reportElement.appendChild(createReportFooterElement(listaTiposCamposInforme,informeVO,dom));
                }
                reportElement.appendChild(createFunctionsElement(dom));
            }

            //print
            OutputFormat format = new OutputFormat(dom);
            format.setEncoding("ISO-8859-1");
            format.setIndenting(true);
            format.setLineWidth(300);

            //to generate output to console use this serializer
            //XMLSerializer serializer = new XMLSerializer(System.out, format);

            File directorioTemp = new File(m_Conf.getString("PDF.base_dir") + File.separator + "tmp");
            if (!directorioTemp.exists()) {
                directorioTemp.mkdirs();
            }
            File informe, fileXML;

            fileXML = File.createTempFile("informe_",".xml",directorioTemp);

            if (formato==0) {
                informe = File.createTempFile("informe_",".pdf",directorioTemp);
            } else if (formato==1) {
                informe = File.createTempFile("informe_",".html",directorioTemp);
            } else if (formato==2) {
                informe = File.createTempFile("informe_",".xls",directorioTemp);
            } else {
                informe = File.createTempFile("informe_",".csv",directorioTemp);
            }

            //to generate a file output use fileoutputstream
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(fileXML),format);

            serializer.serialize(dom);

            m_Log.debug("\n\nINFORME:getAbsolutePath() "+informe.getAbsolutePath()+"\n\n");
            destinoInforme = informe.getAbsolutePath().replace('\\','/');
            m_Log.debug("destinoInforme "+destinoInforme);


            if (accion.equals("generar")) {                
                    exportarPDF(fileXML.getPath(),destinoInforme,informeTableModel);

            } else if (accion.equals("previsualizar")) {
                final PreviewDialog dialog = new PreviewDialog();
                dialog.setReportJob(crearInforme(fileXML.getPath(),destinoInforme,informeTableModel));
                dialog.setSize(500, 500);
                dialog.setModal(true);
                dialog.setVisible(true);                
            }
        } catch(Exception ie) {
            ie.printStackTrace();
        }
    return destinoInforme;
  }

    private MasterReport crearInforme(String fileXML,String destinoInforme,InformeTableModel informeTableModel){
        MasterReport report = null;
        
        try{
           ClassicEngineBoot.getInstance().start();
           ReportGenerator generator = ReportGenerator.getInstance();
           report = generator.parseReport(fileXML);
           report.setDataFactory(new TableDataFactory("SampleQuery", informeTableModel));
           report.setQuery("SampleQuery");
        
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
           e.printStackTrace();
        }

        return report;
    }

    private void exportarPDF(String fileXML,String destinoInforme,InformeTableModel informeTableModel){

        try{

           ClassicEngineBoot.getInstance().start();
           ReportGenerator generator = ReportGenerator.getInstance();
           MasterReport report = generator.parseReport(fileXML);
           report.setDataFactory(new TableDataFactory("SampleQuery", informeTableModel));
           report.setQuery("SampleQuery");
           PdfReportUtil.createPDF(report,destinoInforme);

        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
           e.printStackTrace();
        }
    }

}