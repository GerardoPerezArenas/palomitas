// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.formularios;

// PAQUETES IMPORTADOS
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.arboles.impl.ArbolImpl;

public class FichaFormularioForm extends ActionForm  {
    String codigo;
    String codVisible;
    String version;
    String nombre;
    String codRel;
    String tipo;
    String fAlta;
    String fBaja;
    String unidades;
    String dmte;
    String procedimiento;
    String area;
    String registro;
    String instancia;
    String gTramite;
    String dTramite;
    String instruc;
    String visible;
    private FormFile fichero;
    private FormFile fichero2;
    //Vectores para la construcción de los combos
    Vector listaFormulariosTipo0 = new Vector();
    Vector listaTipos = new Vector();
    Vector listaProcedimientos = new Vector();
    Vector listaAreas = new Vector();
    Vector listaAreasPorDefecto = new Vector();
    Vector listaTramitesProcedimiento = new Vector();
    Vector listaTramites = new Vector();
    Vector listaRestricciones = new Vector();
    Vector listaUORs = new Vector();
    ArbolImpl arbolUORs = new ArbolImpl();
    Vector listaCargos = new Vector();
    ArbolImpl arbolCargos = new ArbolImpl();
    Vector listaPrecargas = new Vector(); //Lista de datos posibles a precargar en el formulario
    Vector precargasSeleccionadas = new Vector(); //Lista de datos seleccionados para precargar en el formulario

    Vector listaUORSRegistroOrigen = new Vector();      // lista con las unidades de registro de origen
    Vector listaUORSRegistroDestino = new Vector();     // lista con las unidades de registro de destino


    private GeneralValueObject formVO = new GeneralValueObject();

    public String getCodigo (){return (String) formVO.getAtributo("codigo");}
    public void setCodigo (String valor) { formVO.setAtributo("codigo",valor);}

    public String getCodVisible (){return (String) formVO.getAtributo("codVisible");}
    public void setCodVisible (String valor) { formVO.setAtributo("codVisible",valor);}

    public String getCodRel (){return (String) formVO.getAtributo("codRel");}
    public void setCodRel (String valor) { formVO.setAtributo("codRel",valor);}

    public String getDmte (){return (String) formVO.getAtributo("dmte");}
    public void setDmte (String valor) { formVO.setAtributo("dmte",valor);}

    public String getdTramite (){return (String) formVO.getAtributo("dTramite");}
    public void setdTramite (String valor) { formVO.setAtributo("dTramite",valor);}

    public String getfAlta (){return (String) formVO.getAtributo("fAlta");}
    public void setfAlta (String valor) { formVO.setAtributo("fAlta",valor);}

    public String getfBaja (){return (String) formVO.getAtributo("fBaja");}
    public void setfBaja (String valor) { formVO.setAtributo("fBaja",valor);}

    public String getgTramite (){return (String) formVO.getAtributo("gTramite");}
    public void setgTramite (String valor) { formVO.setAtributo("gTramite",valor);}

    public String getInstancia (){return (String) formVO.getAtributo("instancia");}
    public void setInstancia (String valor) { formVO.setAtributo("instancia",valor);}

    public String getInstruc (){return (String) formVO.getAtributo("instruc");}
    public void setInstruc (String valor) { formVO.setAtributo("instruc",valor);}

    public String getNombre (){return (String) formVO.getAtributo("nombre");}
    public void setNombre (String valor) { formVO.setAtributo("nombre",valor);}

    public String getProcedimiento (){return (String) formVO.getAtributo("procedimiento");}
    public void setProcedimiento (String valor) { formVO.setAtributo("procedimiento",valor);}

    public String getArea (){return (String) formVO.getAtributo("area");}
    public void setArea (String valor) { formVO.setAtributo("area",valor);}

    public String getRegistro (){return (String) formVO.getAtributo("registro");}
    public void setRegistro (String valor) { formVO.setAtributo("registro",valor);}

    public String getTipo (){return (String) formVO.getAtributo("tipo");}
    public void setTipo (String valor) { formVO.setAtributo("tipo",valor);}

    public String getUnidades (){return (String) formVO.getAtributo("unidades");}
    public void setUnidades (String valor) { formVO.setAtributo("unidades",valor);}

    public String getVersion (){return (String) formVO.getAtributo("version");}
    public void setVersion (String valor) { formVO.setAtributo("version",valor);}

    public String getVisible (){return (String) formVO.getAtributo("visible");}
    public void setVisible (String valor) { formVO.setAtributo("visible",valor);}

    public GeneralValueObject getFormularioVO(){ return formVO; }
    public void setFormularioVO(GeneralValueObject formVO){ this.formVO=formVO;}

    public FormFile getFichero() { return (FormFile) formVO.getAtributo("fichero"); }
    public void setFichero(FormFile fichero) { formVO.setAtributo("fichero",fichero);}

    public FormFile getFichero2() { return (FormFile) formVO.getAtributo("fichero2"); }
    public void setFichero2(FormFile fichero2) { formVO.setAtributo("fichero2",fichero2);}


    public Vector getListaFormulariosTipo0() {
        return listaFormulariosTipo0;
    }

    public void setListaFormulariosTipo0(Vector listaFormulariosTipo0) {
        this.listaFormulariosTipo0 = listaFormulariosTipo0;
    }

    public Vector getListaTipos() {
        return listaTipos;
    }

    public void setListaTipos(Vector listaTipos) {
        this.listaTipos=listaTipos;
    }

    public Vector getListaProcedimientos() {
        return listaProcedimientos;
    }

    public void setListaProcedimientos(Vector listaProcedimientos) {
        this.listaProcedimientos=listaProcedimientos;
    }

    public Vector getListaAreas() {
        return listaAreas;
    }

    public void setListaAreas(Vector listaAreas) {
        this.listaAreas = listaAreas;
    }

    public Vector getListaAreasPorDefecto() {
        return listaAreasPorDefecto;
    }

    public void setListaAreasPorDefecto(Vector listaAreasPorDefecto) {
        this.listaAreasPorDefecto=listaAreasPorDefecto;
    }

    public Vector getListaTramites() {
        return listaTramites;
    }

    public void setListaTramites(Vector listaTramites) {
        this.listaTramites=listaTramites;
    }

    public Vector getListaRestricciones() {
        return listaRestricciones;
    }

    public void setListaRestricciones(Vector listaRestricciones) {
        this.listaRestricciones=listaRestricciones;
    }

    public Vector getListaTramitesProcedimiento() {
        return listaTramitesProcedimiento;
    }

    public void setListaTramitesProcedimiento(Vector listaTramitesProcedimiento) {
        this.listaTramitesProcedimiento=listaTramitesProcedimiento;
    }

    public Vector getListaCargos() {
        return listaCargos;
    }

    public void setListaCargos(Vector listaCargos) {
        this.listaCargos = listaCargos;
    }

    public Vector getListaUORs() {
        return listaUORs;
    }

    public void setListaUORs(Vector listaUORs) {
        this.listaUORs = listaUORs;
    }

    public ArbolImpl getArbolCargos() {
        return arbolCargos;
    }

    public void setArbolCargos(ArbolImpl arbolCargos) {
        this.arbolCargos = arbolCargos;
    }

    public ArbolImpl getArbolUORs() {
        return arbolUORs;
    }

    public void setArbolUORs(ArbolImpl arbolUORs) {
        this.arbolUORs = arbolUORs;
    }
    public Vector getListaPrecargas() {
        return listaPrecargas;
    }

    public void setListaPrecargas(Vector listaPrecargas) {
        this.listaPrecargas = listaPrecargas;
    }
    public Vector getPrecargasSeleccionadas() {
        return precargasSeleccionadas;
    }

    public void setPrecargasSeleccionadas(Vector precargasSeleccionadas) {
        this.precargasSeleccionadas = precargasSeleccionadas;
    }

    public Vector getListaUORSRegistroDestino() {
        return listaUORSRegistroDestino;
    }

    public void setListaUORSRegistroDestino(Vector listaUORSRegistroDestino) {
        this.listaUORSRegistroDestino = listaUORSRegistroDestino;
    }

    public Vector getListaUORSRegistroOrigen() {
        return listaUORSRegistroOrigen;
    }

    public void setListaUORSRegistroOrigen(Vector listaUORSRegistroOrigen) {
        this.listaUORSRegistroOrigen = listaUORSRegistroOrigen;
    }



}