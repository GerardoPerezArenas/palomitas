package es.altia.agora.business.gestionInformes;

import java.util.Collection;
import java.util.Collections;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class InformeTableModel extends DefaultTableModel
{
    protected static Log m_Log =
        LogFactory.getLog(InformeTableModel.class.getName());

    private int NUM_FILAS_INFORME = 50;

    public InformeTableModel () {
        super(new Vector(),new Vector());
    }
    /*
    public void construirInformeTableModel(Vector columnNames, Vector data) {
        columnIdentifiers = columnNames;
        dataVector = data;
        for (int i=0;i<columnIdentifiers.size();i++) {
            if (columnIdentifiers.get(i)==null) {
                columnIdentifiers.set(i,"");
            }
        }
        Vector row;
        for (int i=0;i<dataVector.size();i++) {
            row = (Vector)dataVector.get(i);
            for (int j=0;j<row.size();j++) {
                if (row.get(j)==null) {
                    row.set(j,"");
                }
            }
            dataVector.set(i,row);
        }
    }
    */
    
    public void construirInformeTableModel(Vector columnNames, Vector data) {
        if(m_Log.isDebugEnabled()) m_Log.debug("construirInformeTableModel() : BEGIN");
        columnIdentifiers = columnNames;
        dataVector = data;
        
        if(columnIdentifiers.contains(null)){
            Collections.replaceAll(columnIdentifiers, null, "");
        }//if(columnIdentifiers.contains(null))
        
        Vector row;
        for (int i=0;i<dataVector.size();i++) {
            row = (Vector)dataVector.get(i);
            if(row.contains(null)){
                Collections.replaceAll(row, null, "");
                dataVector.set(i, row);
            }//if(row.contains(null))
        }//for (int i=0;i<dataVector.size();i++)
        
        if(m_Log.isDebugEnabled()) m_Log.debug("construirInformeTableModel() : END");
    }//construirInformeTableModel

    public void rellenarInformeTableModel(Vector columnasInforme) {
        if(m_Log.isDebugEnabled()) m_Log.debug("rellenarInformeTableModel() : BEGIN");
        columnIdentifiers = columnasInforme;
        dataVector = new Vector();
        for (int i=0;i<NUM_FILAS_INFORME;i++) {
            Vector row = new Vector();
            for (int j=0;j<columnIdentifiers.size();j++) {
                row.add("datos");
            }//for
            dataVector.add(row);
        }//for
        if(m_Log.isDebugEnabled()) m_Log.debug("rellenarInformeTableModel() : END");
    }//rellenarInformeTableModel

    public Vector getColumnNames() {
        return columnIdentifiers;
    }//getColumnNames

    public void setColumnNames(Vector columnNames) {
        columnIdentifiers = columnNames;
    }//setColumnNames

    public Vector getDATA() {
        return dataVector;
    }//getDATA

    public void setData(Vector data) {
        dataVector = data;
    }//setData

    public String toString() {
        String salida = new String("");
        /*
         * Comento esto por que en situacion normal es una burrada hacer un to string de un elemento que puede contener miles de filas
         * con miles de columnas de datos en el log.
         * 
        String salida = "\nINFORME TABLE MODEL:\n";
        for (int j=0;j<columnIdentifiers.size();j++) {
            salida = salida.concat("\nCOLUMN_NAMES ["+j+"] "+columnIdentifiers.get(j));
        }
        for (int i=0;i<dataVector.size();i++) {
            Vector row = (Vector)dataVector.get(i);
            for (int j=0;j<row.size();j++) {
                salida = salida.concat("\nDATA ["+i+"]["+j+"] "+((Vector)dataVector.get(i)).get(j));
            }
        }
        */
        return salida;
    }//toString

}
