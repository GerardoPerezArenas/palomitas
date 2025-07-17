package es.altia.agora.interfaces.user.web.integracionsw;

import java.util.Iterator;
import java.util.Vector;

import org.apache.struts.action.ActionForm;

import es.altia.agora.business.integracionsw.ParametroConfigurableVO;

public class ConfiguracionSWTramiteForm extends ActionForm {

    private String tituloOperacion;
    private int codigoOperacion;
    private boolean isObligatoria;
    private long cfo;
    private Vector listaParamsEntrada;
    private Vector listaParamsSalida;
    private String estadoError;
    private String codMunicipio;
    private String txtCodigo;
    private String codigoTramite;

    public String getCodigoTramite() {
		return codigoTramite;
	}

	public void setCodigoTramite(String codigoTramite) {
		this.codigoTramite = codigoTramite;
	}

	public String getCodMunicipio() {
		return codMunicipio;
	}

	public void setCodMunicipio(String codMunicipio) {
		this.codMunicipio = codMunicipio;
	}

	public String getTxtCodigo() {
		return txtCodigo;
	}

	public void setTxtCodigo(String txtCodigo) {
		this.txtCodigo = txtCodigo;
	}

	public String getTituloOperacion() {
        return tituloOperacion;
    }

    public void setTituloOperacion(String tituloOperacion) {
        this.tituloOperacion = tituloOperacion;
    }

    public Vector getListaParamsEntrada() {
        return listaParamsEntrada;
    }

    public void setListaParamsEntrada(Vector listaParamsEntrada) {
        this.listaParamsEntrada = listaParamsEntrada;
    }

    public Vector getListaParamsSalida() {
        return listaParamsSalida;
    }

    public void setListaParamsSalida(Vector listaParamsSalida) {
        this.listaParamsSalida = listaParamsSalida;
    }

    public int getCodigoOperacion() {
        return codigoOperacion;
    }

    public void setCodigoOperacion(int codigoOperacion) {
        this.codigoOperacion = codigoOperacion;
    }



    public boolean getIsObligatoria() {
        return isObligatoria;
    }

    public void setIsObligatoria(boolean obligatoria) {
        isObligatoria = obligatoria;
    }

    public String getEstadoError() {
        return estadoError;
    }

    public void setEstadoError(String estadoError) {
        this.estadoError = estadoError;
    }

	public long getCfo() {
		return cfo;
	}

	public void setCfo(long cfo) {
		this.cfo = cfo;
	}


	
	public String toString() {
		return ("\nCfo: " + cfo + "\nCodigo de Operacion: " + codigoOperacion + "\nTitulo de Operacion: " + tituloOperacion + 
				"\nEs obligatoria : " + isObligatoria + "\nLista Parametros Entrada : \n" + listaParamsToString(listaParamsEntrada) + 
				"\nLista Parametros Salida : \n" + listaParamsToString(listaParamsSalida));
	}
	
	private String listaParamsToString(Vector listaParams){
		String str ="Vacía";
		if (listaParams != null) {
			str ="";
			for (Iterator it=listaParams.iterator();it.hasNext();) {
				ParametroConfigurableVO param = (ParametroConfigurableVO)it.next();
				str += "\t" + param + "\n";
			}
		}
		return str;
	}
	
}
