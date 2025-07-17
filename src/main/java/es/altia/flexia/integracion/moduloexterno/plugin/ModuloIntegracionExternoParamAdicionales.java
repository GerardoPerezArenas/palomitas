/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.plugin;

/**
 *
 * @author laura
 */
public class ModuloIntegracionExternoParamAdicionales {
    private String origenLlamada;
  
  public String getOrigenLlamada()
  {
    return this.origenLlamada;
  }
  
  public void setOrigenLlamada(String origenLlamada)
  {
    this.origenLlamada = origenLlamada;
  }
  
  public String toString()
  {
    StringBuilder resultado = new StringBuilder();
    resultado.append("ModuloIntegracionExternoParamAdicionales {");
    resultado.append("origenLlamada=").append(this.origenLlamada);
    resultado.append("}");
    
    return resultado.toString();
  }
}

