package com.hexcore.cas.rulesystems;

import java.util.List;

/**
 * Class TableEntry

 * @authors Karl Zoller
 */

public class TableEntry
{
	public enum Kind {CONSTANT, VARIABLE, METHOD, PROPERTY, CELL, TYPENAME};
	
	/** Language supported types.
	 * 
	 * Includes internal JVM 7 specified name.
	 * 
	 * @author Karl Zöller
	 *
	 */
	public enum Type 
	{
		INT("I"), 
		INT_ARR("[I"), 
		DOUBLE("D"), 
		DOUBLE_ARR("[D"), 
		NONE(""), 
		NONE_ARR(""), 
		BOOL("Z"), 
		BOOL_ARR("[Z"), 
		CELL("Lcom/hexcore/cas/model/Cell;"), 
		CELL_ARR("[Lcom/hexcore/cas/model/Cell;"),
		VOID("V");
		
		private final String iName;
		
		private Type(String internalName)
		{
			iName = internalName;
		};
		
		public String getInternalName()
		{
			return iName;
		}
	};
	
	
	public Type 			type;
	public Kind				kind;
	public int 				value;				//Constant value
	public int 				offset;
	public ArgList			arguments;
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
	
	public boolean checkArguments(ArgList list)
	{
		List<Type> myTypes = arguments.getList();
		List<Type> otherTypes = list.getList();
		
		if(myTypes.size() != otherTypes.size())
			return false;
		
		for(int i = 0; i < myTypes.size(); i++)
		{
			if(myTypes.get(i) != otherTypes.get(i))
				return false;
		}
		
		return true;
	}
}
