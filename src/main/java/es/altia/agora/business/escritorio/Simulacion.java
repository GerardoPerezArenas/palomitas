package es.altia.agora.business.escritorio;


import java.util.Collection;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Simulacion {

	public Simulacion() {
		baseDatos = new Hashtable();
	}

	public void add(AgendaElement aE) {
		baseDatos.put(aE.getDate()+separator+aE.getHour(),aE);
		if (m_Log.isDebugEnabled()) m_Log.debug("Añadiendo : "+aE.getDate()+separator+aE.getHour()+" -->"+aE.getTitle());
	}

	public Collection get() {
		return baseDatos.values();
	}

	public String buildDate(String day,String month,String year) {
		return day+"/"+month+"/"+year;
    }

    private static Hashtable baseDatos ;
    private static final String separator = " # ";
    protected static Log m_Log =
        LogFactory.getLog(Simulacion.class.getName());

}