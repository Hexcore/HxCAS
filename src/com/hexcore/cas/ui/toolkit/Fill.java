package com.hexcore.cas.ui.toolkit;

public class Fill
{
	public enum Type {NONE, SOLID, VERTICAL_GRADIENT, HORIZONTAL_GRADIENT, IMAGE, IMAGE_COLOUR};
	
	public static final int CENTER = 1;
	
	public static final Fill NONE = new Fill();
	
	private Type		type;
	private Colour[]	colours;
	private Image		image = null;
	private int			flags = 0;
	
	public Fill()
	{
		type = Type.NONE;
	}
	
	public Fill(Colour colour)
	{
		type = Type.SOLID;
		colours = new Colour[1];
		colours[0] = colour;
	}
	
	public Fill(Colour top, Colour bottom)
	{
		type = Type.VERTICAL_GRADIENT;
		colours = new Colour[2];
		colours[0] = top;
		colours[1] = bottom;
	}
	
	public Fill(Type type, Colour a, Colour b)
	{
		this.type = type;
		colours = new Colour[2];
		colours[0] = a;
		colours[1] = b;
	}
	
	public Fill(Image image)
	{
		type = Type.IMAGE;
		this.image = image;
	}
	
	public Fill(Image image, Colour colour)
	{
		type = Type.IMAGE_COLOUR;
		colours = new Colour[1];
		colours[0] = colour;
		this.image = image;
	}	
	
	public void		setFlag(int flag) {flags |= flag;}
	
	public Type 	getType() {return type;}
	public Colour	getColour(int index) {return colours[index];}	
	public Image	getImage() {return image;}
	public int		getFlags() {return flags;}
	
	@Override
	public String toString()
	{
		String out = "Fill<";
		
		if (type == Type.NONE)
			out += "None"; 
		else if (type == Type.SOLID)
			out += "Solid: " + colours[0]; 
		else if (type == Type.VERTICAL_GRADIENT)
			out += "Vertical Gradient: " + colours[0] + ", " + colours[1]; 	
		else if (type == Type.HORIZONTAL_GRADIENT)
			out += "Horizontal Gradient: " + colours[0] + ", " + colours[1]; 	
		else if (type == Type.IMAGE)
			out += "Image: " + image.getFilename(); 	
		else if (type == Type.IMAGE_COLOUR)
			out += "Solid with Image: " + colours[0] + ", " + image.getFilename(); 			
		else
			out += "Unknown";
		
		if ((flags & CENTER) != 0) out += ", Centered";
		
		return out + ">";
	}
}
