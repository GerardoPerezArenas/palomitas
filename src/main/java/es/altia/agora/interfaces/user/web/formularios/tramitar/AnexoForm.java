package es.altia.agora.interfaces.user.web.formularios.tramitar;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Formulario para agregar un anexo a un formulario
 * User: Altia Consultores Date: 14-feb-2007
 */
public class AnexoForm extends ActionForm {
        private String formulario;
        private int anexo;
        private String accion;
        private String descripcion;
        private FormFile file;


       /**
         * Getter from property formulario
         * @return Value of property formulario.
         */
        public String getFormulario() {
            return formulario;
        }
        /**
         * Setter for property formulario.
         * @param formulario New value of property formulario.
         */
        public void setFormulario(String formulario) {
            this.formulario = formulario;
        }

        /**
         * Getter from property descripcion
         * @return Value of property descripcion.
         */
        public String getDescripcion() {
            return descripcion;
        }
        /**
         * Setter for property descripcion.
         * @param descripcion New value of property descripcion.
         */
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        /**
         * Getter for property file.
         * @return Value of property file.
         */
        public org.apache.struts.upload.FormFile getFile() {
            return file;
        }

        /**
         * Setter for property file.
         * @param file New value of property file.
         */
        public void setFile(org.apache.struts.upload.FormFile file) {
            this.file = file;
        }

        public int getAnexo() {
            return anexo;
        }

        public void setAnexo(int anexo) {
            this.anexo = anexo;
        }

        public String getAccion() {
            return accion;
        }

        public void setAccion(String accion) {
            this.accion = accion;
        }

        public void reset(ActionMapping mapping, HttpServletRequest request) {
                this.file = null;
                this.descripcion = null;
                this.formulario = null;
                this.accion=null;
                this.anexo=0;
        }

        public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

            ActionErrors errors = new ActionErrors();

            //es un hidden
            if ((formulario == null) || (formulario.equals(""))) {
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("error.datesRequired"));
            }
            //es un hidden
            if ((accion == null) || (accion.equals(""))) {
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("error.datesRequired"));
            }else if ("add".equals(accion)){
                if ((descripcion == null) || (descripcion.equals(""))) {
                    errors.add("descripcion", new ActionError("error.required"));
                }

                if (file != null){
                    if ((file.getFileName() == null) || (file.getFileName().length() == 0)) {
                        errors.add("file", new ActionError("error.required"));
                    }
                    if (file.getFileSize() <= 0) {
                        errors.add("file", new ActionError("error.invalidfile"));
                    }
                }else{
                    errors.add("file", new ActionError("error.required"));
                }

            }else if ("del".equals(accion)){
                if (anexo == 0) {
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("error.datesRequired"));
                }

            }else{
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("error.datesRequired"));
            }

            return errors;

        }

}
