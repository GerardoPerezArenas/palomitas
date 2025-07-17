package es.altia.agora.business.escritorio;


public class AgendaElement {

   public AgendaElement() {}

   public AgendaElement(String d,String m,String y,String t,String h) {
	   setDate(d,m,y);
	   title = t;
	   hour = h;
   }

   public AgendaElement(String d,String m,String y) {
	   setDate(d,m,y);
   }

   public void setTitle(String t) {
	   title = t;
   }

   public String getTitle() {
	   return title;
   }

   public void setDate(String day,String month,String year) {
	   date = day + "/"+month+"/"+year;
   }

   public String getDate() {
		return date;
   }

   public String getHour() {
	   return hour;
   }

   public void setHour(String h) {
	   hour = h;
   }

   private String title;
   private String hour;
   private String date;

}