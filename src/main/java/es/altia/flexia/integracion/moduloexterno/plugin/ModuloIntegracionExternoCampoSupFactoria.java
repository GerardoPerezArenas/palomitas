package es.altia.flexia.integracion.moduloexterno.plugin;

import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import java.util.ResourceBundle;

/**
 * Factoria de la clase que implementa la interfaz IModuloIntegracionExternoCampoSuplementarioFlexia
 * @author Administrador
 */
public class ModuloIntegracionExternoCampoSupFactoria {
    private static ModuloIntegracionExternoCampoSupFactoria instance = null;
    private final String NAME_FILE_CONFIGURATION                  = "ModulosIntegracion";
    private final String IMPL_CLASS_RECUPERAR_CAMPO_SUPLEMENTARIO = "MODULOINTEGRACION/CAMPOSUPLEMENTARIOS/IMPLCLASS";
    
    private ModuloIntegracionExternoCampoSupFactoria (){}

    public static ModuloIntegracionExternoCampoSupFactoria getInstance(){
        if(instance==null)
            instance = new ModuloIntegracionExternoCampoSupFactoria();

        return instance;
    }


    public IModuloIntegracionExternoCamposFlexia getImplClass(){
        IModuloIntegracionExternoCamposFlexia implClass = null;
        try{
            ResourceBundle bundle = ResourceBundle.getBundle(NAME_FILE_CONFIGURATION);            
            String nameClass      = bundle.getString(IMPL_CLASS_RECUPERAR_CAMPO_SUPLEMENTARIO);
            
            Class clase           = Class.forName(nameClass);
            implClass             = (IModuloIntegracionExternoCamposFlexia)clase.newInstance();

        }catch(Exception e){
            e.printStackTrace();
            implClass = null;
        }

        return implClass;
    }

}