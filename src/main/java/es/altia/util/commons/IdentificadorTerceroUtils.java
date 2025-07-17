package es.altia.util.commons;

public class IdentificadorTerceroUtils {
	public static int esTipoCifNifoNie(String identificador) {
		int tamanhoId = identificador.length();
		String primerCaracter = identificador.substring(0, 1);
		String ultimoCaracter = identificador.substring(tamanhoId-1);
		int tipo = -1;
		
		if(tamanhoId == 9) {
			try {
				Integer.parseInt(primerCaracter); 
				// Si no da error entonces es numero, el documento es dni (tipo 1)
				tipo = 1;
			} catch (NumberFormatException nfeC1) {
				// Si da error entonces es letra, tenemos que comprobar el ultimo caracter
				try {
					Integer.parseInt(ultimoCaracter);
					// Si no da error entonces es numero, el documento es cif (tipo 4)
					tipo = 4;
				} catch (NumberFormatException nfeC9) {
                                        //Si el ultimo caracter es una X, Y o Z el documento es nie (tipo 3), en caso contrario es cif (tipo 4)
                                        if ("X".equals(ultimoCaracter) || "Y".equals(ultimoCaracter) || "Z".equals(ultimoCaracter)) {
                                            tipo = 3;
                                        } else {
                                            tipo = 4;
                                        }
				}
			}
		}
		
		return tipo;
	}
}
