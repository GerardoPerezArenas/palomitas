package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.util.ArrayList;
import java.util.Vector;
import org.apache.log4j.Logger;

public class EstructuraCampoModuloIntegracionVO {
    
    private String codCampo; //código del campo que se va a poder consultar
    private Integer tipoCampo;  //representa el tipo del campo (1->numérico, 2->texto, 3->fecha, 4->texto largo, 5->desplegable)
    private Integer tamanho;    //representa el tamanho del campo 
    private String rotulo; //descripción del campo
    private String consultaSql; //contiene la consulta Sql, asociada para recuperar el numero de expediente filtrando por el valor del campo
    private ArrayList<ValorCampoDesplegableModuloIntegracionVO> valoresDesplegable; //Contiene los valores del campo de tipo desplegable
    private ArrayList<String> listaCodDesplegable = new ArrayList();
    private ArrayList<String> listaDescDesplegable = new ArrayList();
    private String valorConsulta = null;
    // El logger debe ser static para convertir el objeto a json
    private static Logger log = Logger.getLogger(EstructuraCampoModuloIntegracionVO.class);
    
    private String URLPlantilla = null;
    
    public ArrayList<ValorCampoDesplegableModuloIntegracionVO> getValoresDesplegable() {
        return valoresDesplegable;
    }

    public void setValoresDesplegable(ArrayList<ValorCampoDesplegableModuloIntegracionVO> valoresDesplegable) {
        this.valoresDesplegable = valoresDesplegable;
    }
    
    
    public String getCodCampo() {
        return codCampo;
    }

    public void setCodCampo(String codCampo) {
        this.codCampo = codCampo;
    }

    public Integer getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(Integer tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getConsultaSql() {
        return consultaSql;
    }

    public void setConsultaSql(String consultaSql) {
        this.consultaSql = consultaSql;
    }

    public String getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(String valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

   

    public ArrayList<String> getListaCodDesplegable() {
    log.debug("Método getListaDescDesplegable.BEGIN:");       
    ArrayList<String> resultado= new ArrayList<String>();    
    for (int i=0; i<this.valoresDesplegable.size(); i++){
         ValorCampoDesplegableModuloIntegracionVO campo=this.valoresDesplegable.get(i);
         String codigo=campo.getCodigo();
         log.debug("Método getListaCodDesplegable.CODIGO:"+codigo);  
         resultado.add(codigo);
         
    }
    log.debug("Método getListaDescDesplegable.END:");
    return resultado;
    
    }

    public void setListaCodDesplegable(ArrayList listaCodDesplegable) {
        this.listaCodDesplegable = listaCodDesplegable;
    }

    //Vamos a devolver un String, pq esta lista, para que la interprete
    //bien el combo de Flexia, tiene que estar en el siguiente 
    //formato:['iva0nacional', 'iva0estranxeiro']
    public String getListaDescDesplegable() {
    log.debug("Método getListaDescDesplegable.BEGIN:");    
    String aux="[";
     ValorCampoDesplegableModuloIntegracionVO campo= new ValorCampoDesplegableModuloIntegracionVO();
    for (int i=0; i<(this.valoresDesplegable.size()-1); i++){
         campo=this.valoresDesplegable.get(i);
         String descripcion=campo.getDescripcion();
         aux=aux+"'"+descripcion+"'"+ ",";
         log.debug("Aux:"+aux);   
    }
    campo=this.valoresDesplegable.get(valoresDesplegable.size()-1);
    String descripcion=campo.getDescripcion();
    aux=aux+"'"+descripcion+"' ]";
    log.debug("Método getListaDescDesplegable.END:"+ aux);
    
    return aux;
    
    }

    public void setListaDescDesplegable(ArrayList listaDescDesplegable) {
        this.listaDescDesplegable = listaDescDesplegable;
    }

    //Recuperarmos la plantilla en función del tipo del campo
    //Nota para los campos suplementarios se va a la BD.
    public String getURLPlantilla() {
        
        if (this.tipoCampo==1) this.URLPlantilla="/jsp/plantillas/moduloExtension/CampoNumericoME.jsp";
        if (this.tipoCampo==2) this.URLPlantilla="/jsp/plantillas/moduloExtension/CampoTextoME.jsp";
        if (this.tipoCampo==3) this.URLPlantilla="/jsp/plantillas/moduloExtension/CampoFechaME.jsp";
        if (this.tipoCampo==6) this.URLPlantilla="/jsp/plantillas/moduloExtension/CampoDesplegableME.jsp";
        
        return this.URLPlantilla;
    }

    
    /*
    public void setURLPlantilla(String URLPlantilla) {
        this.URLPlantilla = URLPlantilla;
    }
*/
    
    public String toString() {
        String sc = "\n"; // salto de carro

        StringBuffer resultado = new StringBuffer("");

        resultado.append("CodCampo: " + getCodCampo() + sc);
        resultado.append("TipoCampo: " + getTipoCampo() + sc);
        resultado.append("Rotulo: " + getRotulo() + sc);
        resultado.append("Tamanho: " + getTamanho() + sc);
        resultado.append("Consulta SQL: " + getConsultaSql() + sc);
        resultado.append("urlPlantilla: "+ getURLPlantilla() + sc);
        resultado.append("valorConsulta: " +getValorConsulta()+sc);
        return resultado.toString();
    }

   
}