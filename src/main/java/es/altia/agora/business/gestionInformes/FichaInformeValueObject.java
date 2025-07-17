package es.altia.agora.business.gestionInformes;

import es.altia.technical.ValueObject;
import es.altia.technical.ValidationException;
import es.altia.technical.Messages;

import java.io.Serializable;
import java.util.Vector;

public class FichaInformeValueObject implements Serializable, ValueObject {

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(String codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getDescProcedimiento() {
      return descProcedimiento;
    }

    public void setDescProcedimiento(String descProcedimiento) {
      this.descProcedimiento = descProcedimiento;
    }

    public String getCodAmbito() {
        return codAmbito;
    }

    public void setCodAmbito(String codAmbito) {
        this.codAmbito = codAmbito;
    }

    public String getDescAmbito() {
        return descAmbito;
    }

    public void setDescAmbito(String descAmbito) {
        this.descAmbito = descAmbito;
    }

    public String getModAmbito() {
        return modAmbito;
    }

    public void setModAmbito(String modAmbito) {
        this.modAmbito = modAmbito;
    }

    public String getTabAmbito() {
        return tabAmbito;
    }

    public void setTabAmbito(String tabAmbito) {
        this.tabAmbito = tabAmbito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Vector getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(Vector listaCampos) {
        this.listaCampos = listaCampos;
    }

    public Vector getListaCamposDisponibles() {
        return listaCamposDisponibles;
    }

    public void setListaCamposDisponibles(Vector listaCamposDisponibles) {
        this.listaCamposDisponibles = listaCamposDisponibles;
    }

    public Vector getListaCriInf() {
        return listaCriInf;
    }

    public void setListaCriInf(Vector listaCriInf) {
        this.listaCriInf = listaCriInf;
    }

    public Vector getListaCriInfDisponibles() {
        return listaCriInfDisponibles;
    }

    public void setListaCriInfDisponibles(Vector listaCriInfDisponibles) {
        this.listaCriInfDisponibles = listaCriInfDisponibles;
    }

    public Vector getListaCabInf() {
        return listaCabInf;
    }

    public void setListaCabInf(Vector listaCabInf) {
        this.listaCabInf = listaCabInf;
    }

    public Vector getListaCabInfDisponibles() {
        return listaCabInfDisponibles;
    }

    public void setListaCabInfDisponibles(Vector listaCabInfDisponibles) {
        this.listaCabInfDisponibles = listaCabInfDisponibles;
    }

    public Vector getListaPieInf() {
        return listaPieInf;
    }

    public void setListaPieInf(Vector listaPieInf) {
        this.listaPieInf = listaPieInf;
    }

    public Vector getListaPieInfDisponibles() {
        return listaPieInfDisponibles;
    }

    public void setListaPieInfDisponibles(Vector listaPieInfDisponibles) {
        this.listaPieInfDisponibles = listaPieInfDisponibles;
    }

    public Vector getListaCabPag() {
        return listaCabPag;
    }

    public void setListaCabPag(Vector listaCabPag) {
        this.listaCabPag = listaCabPag;
    }

    public Vector getListaCabPagDisponibles() {
        return listaCabPagDisponibles;
    }

    public void setListaCabPagDisponibles(Vector listaCabPagDisponibles) {
        this.listaCabPagDisponibles = listaCabPagDisponibles;
    }

    public Vector getListaPiePag() {
        return listaPiePag;
    }

    public void setListaPiePag(Vector listaPiePag) {
        this.listaPiePag = listaPiePag;
    }

    public Vector getListaPiePagDisponibles() {
        return listaPiePagDisponibles;
    }

    public void setListaPiePagDisponibles(Vector listaPiePagDisponibles) {
        this.listaPiePagDisponibles = listaPiePagDisponibles;
    }

    public Vector getListaUOR() {
        return listaUOR;
    }

    public void setListaUOR(Vector listaUOR) {
        this.listaUOR = listaUOR;
    }

    public Vector getListaUORDisponibles() {
        return listaUORDisponibles;
    }

    public void setListaUORDisponibles(Vector listaUORDisponibles) {
        this.listaUORDisponibles = listaUORDisponibles;
    }

    public Vector getListaProcedimientos() {
        return listaProcedimientos;
    }

    public void setListaProcedimientos(Vector listaProcedimientos) {
        this.listaProcedimientos = listaProcedimientos;
    }

    public Vector getListaAmbitos() {
        return listaAmbitos;
    }

    public void setListaAmbitos(Vector listaAmbitos) {
        this.listaAmbitos = listaAmbitos;
    }

    public String getPublicado() {
        return publicado;
    }

    public void setPublicado(String publicado) {
        this.publicado = publicado;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }

    public String getMargenSup() {
        return margenSup;
    }

    public void setMargenSup(String margenSup) {
        this.margenSup = margenSup;
    }

    public String getMargenInf() {
        return margenInf;
    }

    public void setMargenInf(String margenInf) {
        this.margenInf = margenInf;
    }

    public String getMargenDer() {
        return margenDer;
    }

    public void setMargenDer(String margenDer) {
        this.margenDer = margenDer;
    }

    public String getMargenIzq() {
        return margenIzq;
    }

    public void setMargenIzq(String margenIzq) {
        this.margenIzq = margenIzq;
    }

    public Vector getListaCamposCodigo() {
        return listaCamposCodigo;
    }

    public void setListaCamposCodigo(Vector listaCamposCodigo) {
        this.listaCamposCodigo = listaCamposCodigo;
    }

    public Vector getListaCamposNombre() {
        return listaCamposNombre;
    }

    public void setListaCamposNombre(Vector listaCamposNombre) {
        this.listaCamposNombre = listaCamposNombre;
    }

    public Vector getListaCamposTabla() {
        return listaCamposTabla;
    }

    public void setListaCamposTabla(Vector listaCamposTabla) {
        this.listaCamposTabla = listaCamposTabla;
    }

    public Vector getListaCamposPosx() {
        return listaCamposPosx;
    }

    public void setListaCamposPosx(Vector listaCamposPosx) {
        this.listaCamposPosx = listaCamposPosx;
    }

    public Vector getListaCamposPosy() {
        return listaCamposPosy;
    }

    public void setListaCamposPosy(Vector listaCamposPosy) {
        this.listaCamposPosy = listaCamposPosy;
    }

    public Vector getListaCamposAlign() {
        return listaCamposAlign;
    }

    public void setListaCamposAlign(Vector listaCamposAlign) {
        this.listaCamposAlign = listaCamposAlign;
    }

    public Vector getListaCamposAncho() {
        return listaCamposAncho;
    }

    public void setListaCamposAncho(Vector listaCamposAncho) {
        this.listaCamposAncho = listaCamposAncho;
    }

    public Vector getListaCamposElipsis() {
        return listaCamposElipsis;
    }

    public void setListaCamposElipsis(Vector listaCamposElipsis) {
        this.listaCamposElipsis = listaCamposElipsis;
    }
    public Vector getListaCamposOrden() {
        return listaCamposOrden;
    }

    public void setListaCamposOrden(Vector listaCamposOrden) {
        this.listaCamposOrden = listaCamposOrden;
    }

    public Vector getListaCriInfCodigo() {
        return listaCriInfCodigo;
    }

    public void setListaCriInfCodigo(Vector listaCriInfCodigo) {
        this.listaCriInfCodigo = listaCriInfCodigo;
    }

    public Vector getListaCriInfCondCri() {
        return listaCriInfCondCri;
    }

    public void setListaCriInfCondCri(Vector listaCriInfCondCri) {
        this.listaCriInfCondCri = listaCriInfCondCri;
    }

    public Vector getListaCriInfValor1() {
        return listaCriInfValor1;
    }

    public void setListaCriInfValor1(Vector listaCriInfValor1) {
        this.listaCriInfValor1 = listaCriInfValor1;
    }

    public Vector getListaCriInfValor2() {
        return listaCriInfValor2;
    }

    public void setListaCriInfValor2(Vector listaCriInfValor2) {
        this.listaCriInfValor2 = listaCriInfValor2;
    }

    public Vector getListaCriInfTitulo() {
        return listaCriInfTitulo;
    }

    public void setListaCriInfTitulo(Vector listaCriInfTitulo) {
        this.listaCriInfTitulo = listaCriInfTitulo;
    }

    public Vector getListaCriInfOrigen() {
        return listaCriInfOrigen;
    }

    public void setListaCriInfOrigen(Vector listaCriInfOrigen) {
        this.listaCriInfOrigen = listaCriInfOrigen;
    }

    public Vector getListaCriInfTabla() {
        return listaCriInfTabla;
    }

    public void setListaCriInfTabla(Vector listaCriInfTabla) {
        this.listaCriInfTabla = listaCriInfTabla;
    }

    public Vector getListaCabInfCodigo() {
        return listaCabInfCodigo;
    }

    public void setListaCabInfCodigo(Vector listaCabInfCodigo) {
        this.listaCabInfCodigo = listaCabInfCodigo;
    }

    public Vector getListaCabInfPosx() {
        return listaCabInfPosx;
    }

    public void setListaCabInfPosx(Vector listaCabInfPosx) {
        this.listaCabInfPosx = listaCabInfPosx;
    }

    public Vector getListaCabInfPosy() {
        return listaCabInfPosy;
    }

    public void setListaCabInfPosy(Vector listaCabInfPosy) {
        this.listaCabInfPosy = listaCabInfPosy;
    }

    public Vector getListaPieInfCodigo() {
        return listaPieInfCodigo;
    }

    public void setListaPieInfCodigo(Vector listaPieInfCodigo) {
        this.listaPieInfCodigo = listaPieInfCodigo;
    }

    public Vector getListaCabPagCodigo() {
        return listaCabPagCodigo;
    }

    public void setListaCabPagCodigo(Vector listaCabPagCodigo) {
        this.listaCabPagCodigo = listaCabPagCodigo;
    }

    public Vector getListaCabPagPosx() {
        return listaCabPagPosx;
    }

    public void setListaCabPagPosx(Vector listaCabPagPosx) {
        this.listaCabPagPosx = listaCabPagPosx;
    }

    public Vector getListaCabPagPosy() {
        return listaCabPagPosy;
    }

    public void setListaCabPagPosy(Vector listaCabPagPosy) {
        this.listaCabPagPosy = listaCabPagPosy;
    }

    public Vector getListaPiePagCodigo() {
        return listaPiePagCodigo;
    }

    public void setListaPiePagCodigo(Vector listaPiePagCodigo) {
        this.listaPiePagCodigo = listaPiePagCodigo;
    }

    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception es.altia.technical.ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
        boolean correcto = true;
        Messages errors = new Messages();
        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    private String opcion;
    private String codPlantilla;
    private String codMunicipio;
    private String codProcedimiento;
    private String descProcedimiento;
    private String codAmbito;
    private String descAmbito;
    private String modAmbito;
    private String tabAmbito;
    private Vector listaCampos;
    private Vector listaCamposDisponibles;
    private Vector listaCriInf;
    private Vector listaCriInfDisponibles;
    private Vector listaCabInf;
    private Vector listaCabInfDisponibles;
    private Vector listaPieInf;
    private Vector listaPieInfDisponibles;
    private Vector listaCabPag;
    private Vector listaCabPagDisponibles;
    private Vector listaPiePag;
    private Vector listaPiePagDisponibles;
    private Vector listaUOR;
    private Vector listaUORDisponibles;
    private Vector listaProcedimientos;
    private Vector listaAmbitos;
    private String nombre;
    private String publicado;
    private String papel;
    private String orientacion;
    private String margenSup;
    private String margenInf;
    private String margenDer;
    private String margenIzq;

    private Vector listaCamposCodigo;
    private Vector listaCamposNombre;
    private Vector listaCamposTabla;
    private Vector listaCamposPosx;
    private Vector listaCamposPosy;
    private Vector listaCamposAlign;
    private Vector listaCamposAncho;
    private Vector listaCamposElipsis;
    private Vector listaCamposOrden;

    private Vector listaCriInfCodigo;
    private Vector listaCriInfCondCri;
    private Vector listaCriInfValor1;
    private Vector listaCriInfValor2;
    private Vector listaCriInfTitulo;
    private Vector listaCriInfOrigen;
    private Vector listaCriInfTabla;

    private Vector listaCabInfCodigo;
    private Vector listaCabInfPosx;
    private Vector listaCabInfPosy;

    private Vector listaPieInfCodigo;

    private Vector listaCabPagCodigo;
    private Vector listaCabPagPosx;
    private Vector listaCabPagPosy;

    private Vector listaPiePagCodigo;

/* ******************************************************** */
}
