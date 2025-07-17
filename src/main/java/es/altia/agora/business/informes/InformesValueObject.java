package es.altia.agora.business.informes;

import es.altia.technical.ValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;

public class InformesValueObject implements Serializable, ValueObject {

  private boolean error; 

  private String msgError;


  public InformesValueObject() {
    super();
  }

  public boolean getError () { return error; }
  public String getMsgError() { return msgError; }

  public void validate(String idioma) throws ValidationException {
  }

  public String toXml(Vector estadisticas,Vector titulos,String verPend, String verFin,
                      String verVol, String tiempo) {

    StringBuffer textoXml = new StringBuffer("");
    textoXml.append ("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
    textoXml.append ("<informe>");
    if (verPend.equals("true")) textoXml.append("<verPend>"+verPend+"</verPend>");
    if (verFin.equals("true")) textoXml.append("<verFin>"+verFin+"</verFin>");
    if (tiempo!=null) textoXml.append("<tiempo>"+tiempo+"</tiempo>");
    if (verVol.equals("true")) textoXml.append("<verVol>"+verVol+"</verVol>");

    textoXml.append("<titulos>");
    for (int i=0; i<titulos.size(); i++){
      String titulo = (String)titulos.elementAt(i);
      if (titulo.equals("inf_UTR")) titulo = "UNIDAD TRAM.";
      if (titulo.equals("inf_TRA")) titulo = "TRAMITE";
      if (titulo.equals("inf_TPR")) titulo = "TIPO PROCED.";
      if (titulo.equals("inf_PRO")) titulo = "PROCEDIMIENTO";
      if (titulo.equals("inf_CLS")) titulo = "ESTADO";
      if (titulo.equals("inf_ARE")) titulo = "AREA";
      textoXml.append("<titulo><valor>").append(titulo).append("</valor></titulo>");
    }
    textoXml.append("</titulos>");

    textoXml.append("<lineas>");

    while (!estadisticas.isEmpty()){
      textoXml.append("<linea>");
      GeneralValueObject gVO = (GeneralValueObject)estadisticas.remove(0);
      Vector agrupaciones = (Vector)gVO.getAtributo("agrupaciones");
      textoXml.append("<agrupaciones>");
      for (int i=0; i<agrupaciones.size() ; i++){
        String agrupacion = (String)agrupaciones.elementAt(i);
        textoXml.append("<agrupacion><valor>").append(agrupacion).append("</valor></agrupacion>");
      }
      textoXml.append("</agrupaciones>");

      String valor = (String)gVO.getAtributo("tareasPendientes");
      textoXml.append("<tareasPendientes>"+valor+"</tareasPendientes>");
      valor = (String)gVO.getAtributo("expedientesPendientes");
      textoXml.append("<expedientesPendientes>"+valor+"</expedientesPendientes>");
      valor = (String)gVO.getAtributo("tiemposPendientes");
      textoXml.append("<tiemposPendientes>"+valor+"</tiemposPendientes>");

      valor = (String)gVO.getAtributo("tareasHistoricas");
      textoXml.append("<tareasHistoricas>"+valor+"</tareasHistoricas>");
      valor = (String)gVO.getAtributo("expedientesHistoricos");
      textoXml.append("<expedientesHistoricos>"+valor+"</expedientesHistoricos>");
      valor = (String)gVO.getAtributo("tiemposHistoricos");
      textoXml.append("<tiemposHistoricos>"+valor+"</tiemposHistoricos>");

      valor = (String)gVO.getAtributo("expedientesCerrados");
      textoXml.append("<expedientesCerrados>"+valor+"</expedientesCerrados>");
      valor = (String)gVO.getAtributo("expedientesEnTramitacion");
      textoXml.append("<expedientesEnTramitacion>"+valor+"</expedientesEnTramitacion>");
      valor = (String)gVO.getAtributo("expedientesTotales");
      textoXml.append("<expedientesTotales>"+valor+"</expedientesTotales>");

      textoXml.append("</linea>");
    }

    textoXml.append("</lineas>");

    textoXml.append ("</informe>");

    return textoXml.toString();
  }
}
