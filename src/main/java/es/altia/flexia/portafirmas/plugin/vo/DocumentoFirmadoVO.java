package es.altia.flexia.portafirmas.plugin.vo;

import java.io.File;

public class DocumentoFirmadoVO {
    
    private String firma;
    private String ficheroHash64;
    private File ficheroFirma;
    private String tipoMime;

    public File getFicheroFirma() {
        return ficheroFirma;
    }

    public void setFicheroFirma(File ficheroFirma) {
        this.ficheroFirma = ficheroFirma;
    }

    public String getFicheroHash64() {
        return ficheroHash64;
    }

    public void setFicheroHash64(String ficheroHash64) {
        this.ficheroHash64 = ficheroHash64;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }
    
    public String getTipoMime() {
        return tipoMime;
    }
    
    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }
    
}
