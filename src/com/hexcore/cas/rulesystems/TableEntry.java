package com.hexcore.cas.rulesystems;

public class TableEntry
{
	public static final int	Constant = 0,		//Kinds
							Variable = 1,
							sFunction = 2,
							aFunction = 3,							
							Property = 4,		//For cell properties
							Cell =     5,
							
							intType = 0,		//Types
							doubleType = 2,
							noType = 4,
							boolType = 6,
							cellType = 8;
	
	public int 				type;
	public int 				kind;				
	public int 				value;				//Constant value
	public int 				offset;
	public String			name;
	
	public TableEntry 		nextEntry;
	
	
	
	public TableEntry()
	{
		nextEntry = null;
	}
	
	public static boolean isArith(int type)
	{
		if(type <= 3)
			return true;
		else
			return false;
	}
	
	public static boolean isBool(int type)
	{
		if(type == 6 || type == 7)
			return true;
		else
			return false;
	}
	
	public static boolean isArray(int type)
	{
		if(type == 1 || type == 3 || type == 9)
			return true;
		else
			return false;
	}
	
	public static boolean isFunction(int kind)
	{
		if(kind == 2 || kind == 3)
			return true;
		else
			return false;
	}
}
