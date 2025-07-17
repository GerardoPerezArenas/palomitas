package es.altia.agora.business.escritorio;

import java.io.Serializable;

public final class UserPreferences implements Serializable
{

	private String backgroundColor;
    private String letterColor;
    private String fontColor;
    private String fontSize;
    private String font;
	
	public UserPreferences() {
		setDefault();
	 }

	public void setDefault() {
		setBackgroundColor("#3A6EA5");
		setFont("Tahoma, Arial, Verdana");
		setFontColor("#FFFFFF");
		setFontSize("-1");
	}

    public void setBackgroundColor(String color) { backgroundColor = color; }
    public String getBackgroundColor() { return backgroundColor; }

	public void setLetterColor(String color) { letterColor = color; }
    public String getLetterColor() { return letterColor; }

    public void setFontColor(String color) { fontColor = color; }
    public String getFontColor() { return fontColor; }

    public void setFontSize(String size) { fontSize = size; }
    public String getFontSize() { return fontSize; }

    public void setFont(String f) { font = f; }
    public String getFont() { return font; }     

}