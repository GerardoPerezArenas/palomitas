package es.altia.agora.business.sge.plugin.documentos;

import java.util.Hashtable;


public interface RepositorioDocumentacionListener {    
    //public void recargarPluginProcedimiento(Hashtable<String,AlmacenDocumento> pluginProcedimientos);
    //public void recargarPluginRegistro(AlmacenDocumento almacen);    
    public void recargarPluginProcedimiento(String codOrganizacion,Hashtable<String,AlmacenDocumento> pluginProcedimientos);    
    public void recargarPluginRegistro(String codOrganizacion,AlmacenDocumento almacen);        
    
}
