package es.altia.agora.business.geninformes.utils.valFormato;

public class ValForGeneral

{
    public static void ValidaFormatoNomeApelido(String nome) throws Exception
    {
        	if (nome != null)
			{
				if (nome.equals(""))
					throw new es.altia.agora.business.geninformes.utils.ExceptionXade(11);
			}
			else
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(11);
    }
	
	public static void ValidaFormatoTipoIdentificacion(String trat) throws Exception
	{
		if ( (trat==null) || (trat.trim().equals("")) ) throw new es.altia.agora.business.geninformes.utils.ExceptionXade(56);
	}
	
	public static void ValidaFormatoIdentificacion(String vardidcod,String vardidnum) throws Exception
	{
		if ( (vardidcod!=null) && (vardidnum!=null) ) {
			if (vardidcod.equals("DM") || vardidcod.equals("DA") || vardidcod.equals("DP") || vardidcod.equals("DT") ) {
				ValidaFormatoNIFConLetra(vardidnum);
				} else if (vardidnum.equals("")) throw new es.altia.agora.business.geninformes.utils.ExceptionXade(55);
			
		} else throw new es.altia.agora.business.geninformes.utils.ExceptionXade(55);
	}
	
//	public static String ValidaFormatoFecha(String fecha) throws Exception
//	{
//	    es.altia.util.Debug.println("La fecha a validar__"+fecha);
//	    es.altia.util.Fecha fec = null;
//	    
//		if ( (fecha!=null) && (!fecha.equals("")) && (fecha.length()==10 || fecha.length()==8 || fecha.length()==9) ) {
//			try {
//				    fec=new es.altia.util.Fecha(fecha);
//				}
//			catch (Exception e) {
//				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(57,e,"");
//			}
//				
//		} else	throw new es.altia.agora.business.geninformes.utils.ExceptionXade(57);
//		
//		return fec==null?"":fec.toStringCorto();		
//		
//	}
	
	public static void ValidaFormatoSexo(String sexo) throws Exception
	{
		if (sexo!=null) {
			if (! ( (sexo.equals("M")) || (sexo.equals("V")) ) )
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(58);
		} else	throw new es.altia.agora.business.geninformes.utils.ExceptionXade(58);
		
	}
	
	public static void ValidaFormatoTratamento(String trat) throws Exception
	{
		if ( (trat==null) || (trat.trim().equals("")) ) throw new es.altia.agora.business.geninformes.utils.ExceptionXade(59);
	}
	
	public static void ValidaFormatoConcelloEnderezo(String conc) throws Exception
	{
		if ( (conc==null) || (conc.trim().equals("")) ) throw new es.altia.agora.business.geninformes.utils.ExceptionXade(60);
	}
	
	public static void ValidaFormatoEnderezo(String conc) throws Exception
	{
		if ( (conc==null) || (conc.trim().equals("")) )  throw new es.altia.agora.business.geninformes.utils.ExceptionXade(61);
	}
	
	public static void ValidaFormatoTipoDomicilio(String conc) throws Exception
	{
		if ( (conc==null)|| (conc.trim().equals("")) ) throw new es.altia.agora.business.geninformes.utils.ExceptionXade(62);
	}
	
	
//	public static void ValidaPeriodo(String varfecalt,String varfecbaj) throws Exception
//	{
//		es.altia.util.Fecha fecalt;
//		es.altia.util.Fecha fecbaj;
//		try {
//			fecalt=new es.altia.util.Fecha(varfecalt);
//			fecbaj=new es.altia.util.Fecha(varfecbaj);
//		} catch (Exception e) {
//			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(76,e,"");
//		}
//		if (es.altia.util.Fecha.ComparaDosFechas(fecalt,fecbaj)==-1)
//			throw new es.altia.agora.business.geninformes.utils.ExceptionXade(76);
//	}

	//
	//	Acepta NIF's de la forma NNNNNNNNL
	//  donde N es un dígito 0-9
	//  y L es una letra
	//
	private static void  ValidaFormatoNIFConLetra(String nif) throws Exception
	{
		int i=0;
		boolean mal=false;
		if ( (nif.length()==9) && (Character.isLetter(nif.charAt(8))) ) {
			while ( (i<8) && (!mal) ) {
				mal=!Character.isDigit(nif.charAt(i));
				i++;
			}
		} else mal=true;
		
		if (mal) throw new es.altia.agora.business.geninformes.utils.ExceptionXade(54);
	
	}
	
	public static void ValidaFormatoNRP(String nrp) throws Exception
    {
		if ( (nrp==null) || (nrp.trim().equals("")) )  throw new es.altia.agora.business.geninformes.utils.ExceptionXade(92);
   
    }
	public static boolean ValidaHora(String hora) throws Exception
    {
	    //es.altia.util.Debug.println("La hora validar__"+hora);
	    
		if ( (hora==null) || (hora.trim().equals("")) ||
		    (Integer.parseInt(hora.trim()) < 0 )||(Integer.parseInt(hora.trim()) > 23 ))
		throw new es.altia.agora.business.geninformes.utils.ExceptionXade(98);
		return true;
    }
	public static boolean ValidaMinuto(String minuto) throws Exception
    {
	    //es.altia.util.Debug.println("El minuto validar__"+minuto);
        
		if ( (minuto==null) || (minuto.trim().equals("")) ||
		    (Integer.parseInt(minuto.trim()) < 0 )||(Integer.parseInt(minuto.trim()) > 59 ))
		throw new es.altia.agora.business.geninformes.utils.ExceptionXade(99);
		return true;
    }

	public static void ValidaFormatoHoraMinuto(String minutejo) throws Exception
    {
	    //es.altia.util.Debug.println("La hora y minuto__"+minutejo);
	    //es.altia.util.Debug.println("Longetud__"+(minutejo.trim().length()));
		if ( (minutejo==null) || (minutejo.trim().equals("")) ||(minutejo.trim().length()!= 4)||
		    !ValidaHora(minutejo.trim().substring(0,2))||!ValidaMinuto(minutejo.trim().substring(2,4)))
		throw new es.altia.agora.business.geninformes.utils.ExceptionXade(104);
    }
	public static void ValidaFormatoAno(String ano) throws Exception
    {
		int anonum;
		if ( (ano!=null) && (!ano.equals("")) && (ano.length()==4) ) {
			try {
				    anonum=Integer.parseInt(ano);
				}
			catch (Exception e) {
				throw new es.altia.agora.business.geninformes.utils.ExceptionXade(114,e,"");
			}
				
		} else	throw new es.altia.agora.business.geninformes.utils.ExceptionXade(114);
		
        
    }

}


