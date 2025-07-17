/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.lanbide.impresion;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

/**
 *
 * @author SantiagoC
 */
public class Subreport 
{
    private JasperReport subreport;

    private JRXmlDataSource datasource;


    private Map subreportParams;

    public Subreport(JasperReport subreport, JRXmlDataSource dataSource, Map subreportParams)
    {
        this.datasource = dataSource;
        this.subreport = subreport;
        this.subreportParams = subreportParams;
    }

    public JasperReport getSubreport() {
        return subreport;
    }

    public void setSubreport(JasperReport subreport) {
        this.subreport = subreport;
    }

    public JRXmlDataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(JRXmlDataSource dataSource) {
        this.datasource = dataSource;
    }

    public Map getSubreportParams() {
        return subreportParams;
    }

    public void setSubreportParams(Map subreportParams) {
        this.subreportParams = subreportParams;
    }    
}
