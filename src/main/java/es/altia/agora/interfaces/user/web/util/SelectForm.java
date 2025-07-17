package es.altia.agora.interfaces.user.web.util;

import es.altia.agora.business.select.SelectJoinValueObject;
import es.altia.agora.business.select.SelectValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** Clase utilizada para capturar o mostrar el estado de una Select */
public class SelectForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
        protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log = LogFactory.getLog(SelectForm.class.getName());
    //Reutilizamos
    SelectValueObject laSelect = new SelectValueObject();
    SelectJoinValueObject laSelectJoin = new SelectJoinValueObject();

    public SelectValueObject getSelect() {
        return laSelect;
    }

    public void setSelect(SelectValueObject laSelect) {
        this.laSelect = laSelect;
    }

    public SelectJoinValueObject getSelectJoin() {
        return laSelectJoin;
    }

    public void setSelectJoin(SelectJoinValueObject laSelectJoin) {
        this.laSelectJoin = laSelectJoin;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getCol_cod(){
        return laSelect.getCol_cod();
    }

    public void setCol_cod(String col_cod){
    	laSelect.setCol_cod(col_cod.trim());
    }

    public String getCol_desc_c(){
        return laSelect.getCol_desc_c();
    }

    public void setCol_desc_c(String col_desc_c){
    	laSelect.setCol_desc_c(col_desc_c.trim());
    }

    public String getCol_desc_e(){
        return laSelect.getCol_desc_e();
    }

    public void setCol_desc_e(String col_desc_e){
    	laSelect.setCol_desc_e(col_desc_e.trim());
    }

    public String getNom_tabla(){
        return laSelect.getNom_tabla();
    }

    public void setNom_tabla(String nom_tabla){
    	laSelect.setNom_tabla(nom_tabla.trim());
    }

    public String getCol_where(){
        return laSelect.getCol_where();
    }

    public void setCol_where(String col_where){
    	laSelect.setCol_where(col_where.trim());
    }

    public String getValor_where(){
        return laSelect.getValor_where();
    }

    public void setValor_where(String valor_where){
    	laSelect.setValor_where(valor_where.trim());
    }

    public String getCol_where2(){
        return laSelect.getCol_where2();
    }

    public void setCol_where2(String col_where2){
    	laSelect.setCol_where2(col_where2.trim());
    }

    public String getValor_where2(){
        return laSelect.getValor_where2();
    }

    public void setValor_where2(String valor_where2){
    	laSelect.setValor_where2(valor_where2.trim());
    }


    public String getValor_desc(){
        return laSelect.getValor_desc();
    }

    public void setValor_desc(String valor_desc){
    	laSelect.setValor_desc(valor_desc.trim());
    }



    /* Seccion donde metemos los metods get y set de los inputs de SelectValueObject*/
    public String getInput_cod(){
        return laSelect.getInput_cod();
    }

    public void setInput_cod(String input_cod){
    	laSelect.setInput_cod(input_cod.trim());
    }

    public String getInput_desc(){
        return laSelect.getInput_desc();
    }

    public void setInput_desc(String input_desc){
    	laSelect.setInput_desc(input_desc.trim());
    }


	public void setTarget1(String target) {
			laSelect.setTarget1(target);
	}
	public String getTarget1() {
			return laSelect.getTarget1();
	}

    /* Seccion donde metemos los metods get y set de los inputs de SelectJoinValueObject*/
    public String getInput_cod_join(){
        return laSelectJoin.getInput_cod_join();
    }

    public void setInput_cod_join(String input_cod_join){
    	laSelectJoin.setInput_cod_join(input_cod_join.trim());
    }

    public String getInput_desc_join(){
        return laSelectJoin.getInput_desc_join();
    }

    public void setInput_desc_join(String input_desc_join){
    	laSelectJoin.setInput_desc_join(input_desc_join.trim());
    }


    //Metodos get y set para cargar y devolver las listas
    public Vector getLista_resultado() {
       return laSelect.getLista_resultado();
    }

    public void setLista_resultado(Vector lista_resultado){
       laSelect.setLista_resultado(lista_resultado);
    }

    public Vector getLista_resultado_join() {
       return laSelectJoin.getLista_resultado_join();
    }

    public void setLista_resultado_join(Vector lista_resultado_join){
       laSelectJoin.setLista_resultado_join(lista_resultado_join);
    }


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
		boolean correcto = true;
        ActionErrors errors = new ActionErrors();
        //SelectValueObject hara el trabajo para nostros ...
        try {
            laSelect.validate(idioma);
        } catch (ValidationException ve) {
            //Hay errores...
            //Tenemos que traducirlos a formato struts
            Iterator iter = ve.getMessages().get();
            while (iter.hasNext()) {
                Message message = (Message)iter.next();
                errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
            }
        }
        return errors;
    }
}
