package es.altia.flexia.interfaces.user.web.registro.informes;

import es.altia.agora.technical.ConstantesDatos;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class EjecutaJustificantePDFFactoria {
    private static EjecutaJustificantePDFFactoria instance = null;
    private Logger log = Logger.getLogger(EjecutaJustificantePDFFactoria.class);

    private EjecutaJustificantePDFFactoria(){}

    public static EjecutaJustificantePDFFactoria getInstance(){
        if(instance==null)
            instance = new EjecutaJustificantePDFFactoria();

        return instance;
    }
    

    /**
     * Devuelve la implementación de la clase que se encarga de mostrar el PDF con el justificante de registro
     * @param codOrganizacion: Código de la organización
     * @return IEjecutaJustificantePDF o null sino se ha podido recuperar
     */
    public IEjecutaJustificantePDF getImplClass(int codOrganizacion){

        IEjecutaJustificantePDF implClass = null;
        try{
            ResourceBundle config = ResourceBundle.getBundle("Registro");
            String nameClass = config.getString(codOrganizacion + ConstantesDatos.JUSTIFICANTE_REGISTRO_IMPL_CLASS);

            Class clase = Class.forName(nameClass);
            implClass = (IEjecutaJustificantePDF)clase.newInstance();

        }catch(Exception e){
            log.error("Error al instanciar la clase que ejecuta el pdf del justificante de registro: " + e.getMessage());
            implClass = null;
        }

        return implClass;
    }


}