package com.hexcore.cas.rulesystems;

/**
 * Class TableEntry

 * @authors Karl Zoller
 */

public class TableEntry
{
	public enum Kind {CONSTANT, VARIABLE, SFUNCTION, AFUNCTION, PROPERTY, CELL, TYPENAME};
	public enum Type {INT, INT_ARR, DOUBLE, DOUBLE_ARR, NONE, NONE_ARR, BOOL, BOOL_ARR, CELL, CELL_ARR};
	
	
	public Type 			type;
	public Kind				kind;
	public int 				value;				//Constant value
	public int 				offset;
	public Type				argType;
	public String			name;
	public boolean			immutable = false;
	
	public TableEntry 		nextEntry;
	
	public TableEntry()
	{
		nextEntry = null;
		type = Type.NONE;
	}
	
	public static boolean isArith(Type type)
	{
		if(type.ordinal() <= 3)
			return true;
		else
			return false;
	}
	
	public static boolean isBool(Type type)
	{
		if(type == Type.BOOL || type == Type.BOOL_ARR)
			return true;
		else
			return false;
	}
	
	public static boolean isArray(Type type)
	{
		if(type == Type.INT_ARR|| type == Type.DOUBLE_ARR || type == Type.BOOL_ARR || type == Type.CELL_ARR)
			return true;
		else
			return false;
	}
	
	public static boolean isScalar(Type type)
	{
		if(type.ordinal() % 2 == 0)
			return true;
		else
			return false;
	}
	
	
	public static boolean isFunction(Kind kind)
	{
		if(kind == Kind.AFUNCTION || kind == Kind.SFUNCTION)
			return true;
		else
			return false;
	}
}
