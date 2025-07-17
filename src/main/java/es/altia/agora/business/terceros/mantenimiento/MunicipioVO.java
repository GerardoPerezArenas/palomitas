package es.altia.agora.business.terceros.mantenimiento;

public class MunicipioVO {

    private int codigoPais;
    private int codigoProvincia;
    private int codigoMunicipio;
    private int partidoJudicial;
    private int comarca;
    private String nombreOficial;
    private String nombreLargo;
    private String digitoControl;
    private float superficie;
    private int altitud;
    private int kmsCapital;
    private float latitudNorte;
    private float latitudSur;
    private float longitudEste;
    private float longitudOeste;
    private String situacion;

    public MunicipioVO() {
    }

    public MunicipioVO(int codigoPais, int codigoProvincia, int codigoMunicipio, int partidoJudicial, int comarca,
                       String nombreOficial, String nombreLargo, String digitoControl, float superficie, int altitud,
                       int kmsCapital, float latitudNorte, float latitudSur, float longitudEste, float longitudOeste,
                       String situacion) {

        this.codigoPais = codigoPais;
        this.codigoProvincia = codigoProvincia;
        this.codigoMunicipio = codigoMunicipio;
        this.partidoJudicial = partidoJudicial;
        this.comarca = comarca;
        this.nombreOficial = nombreOficial;
        this.nombreLargo = nombreLargo;
        this.digitoControl = digitoControl;
        this.superficie = superficie;
        this.altitud = altitud;
        this.kmsCapital = kmsCapital;
        this.latitudNorte = latitudNorte;
        this.latitudSur = latitudSur;
        this.longitudEste = longitudEste;
        this.longitudOeste = longitudOeste;
        this.situacion = situacion;
    }

    public int getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(int codigoPais) {
        this.codigoPais = codigoPais;
    }

    public int getCodigoProvincia() {
        return codigoProvincia;
    }

    public void setCodigoProvincia(int codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    public int getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(int codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public int getPartidoJudicial() {
        return partidoJudicial;
    }

    public void setPartidoJudicial(int partidoJudicial) {
        this.partidoJudicial = partidoJudicial;
    }

    public int getComarca() {
        return comarca;
    }

    public void setComarca(int comarca) {
        this.comarca = comarca;
    }

    public String getNombreOficial() {
        return nombreOficial;
    }

    public void setNombreOficial(String nombreOficial) {
        this.nombreOficial = nombreOficial;
    }

    public String getNombreLargo() {
        return nombreLargo;
    }

    public void setNombreLargo(String nombreLargo) {
        this.nombreLargo = nombreLargo;
    }

    public String getDigitoControl() {
        return digitoControl;
    }

    public void setDigitoControl(String digitoControl) {
        this.digitoControl = digitoControl;
    }

    public float getSuperficie() {
        return superficie;
    }

    public void setSuperficie(float superficie) {
        this.superficie = superficie;
    }

    public int getAltitud() {
        return altitud;
    }

    public void setAltitud(int altitud) {
        this.altitud = altitud;
    }

    public int getKmsCapital() {
        return kmsCapital;
    }

    public void setKmsCapital(int kmsCapital) {
        this.kmsCapital = kmsCapital;
    }

    public float getLatitudNorte() {
        return latitudNorte;
    }

    public void setLatitudNorte(float latitudNorte) {
        this.latitudNorte = latitudNorte;
    }

    public float getLatitudSur() {
        return latitudSur;
    }

    public void setLatitudSur(float latitudSur) {
        this.latitudSur = latitudSur;
    }

    public float getLongitudEste() {
        return longitudEste;
    }

    public void setLongitudEste(float longitudEste) {
        this.longitudEste = longitudEste;
    }

    public float getLongitudOeste() {
        return longitudOeste;
    }

    public void setLongitudOeste(float longitudOeste) {
        this.longitudOeste = longitudOeste;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }
}
