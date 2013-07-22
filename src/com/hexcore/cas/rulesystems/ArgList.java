package com.hexcore.cas.rulesystems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hexcore.cas.rulesystems.TableEntry.Type;

/** Wraps a list of types that represent the types of function arguments.
 * Needed due to the restrictions of the CoCo parser.
 * 
 * @author Karl Zöller
 *
 */
public class ArgList
{
	private List<Type> types;
	
	/*
	 * Default constructor.
	 */
	public ArgList()
	{
		types = new ArrayList<Type>();
	}
	
	/*
	 * Constructs an ArgList from the given array of Types.
	 */
	public ArgList(Type... t)
	{
		types = new ArrayList<Type>();
		
		for(int i = 0; i < t.length; i++)
			types.add(t[i]);
	}
	
	
	/*
	 * Adds a type to the end of the argument list.
	 */
	public void add(Type t)
	{
		types.add(t);
	}
	
	/*
	 * Removes a type from the specified index of the argument list.
	 */
	public Type remove(int index)
	{
		return types.remove(index);
	}
	
	/*
	 * Removes the first occurrence of the specified type from the argument list.
	 */
	public boolean remove(Type t)
	{
		return types.remove(t);
	}
	
	public void clear()
	{
		types.clear();
	}
	
	/*
	 * Gets the internal List that makes up this argument list.
	 */
	public List<Type> getList()
	{
		return types;
	}
	
	@Override
	public String toString()
	{
		String res = "";
		
		for(int i = 0; i < types.size(); i++)
			res += types.toString() + "\n";
		
		return res;
	}
	
	/*
	 * Gets the internal representation of this argument list.
	 * Constructs and returns the JVM 7 specified internal parameter descriptor
	 * in a ready to use form.
	 * Example:
	 * If the arguments of a function are of types: int, double[], Object, int
	 * then this function would return:
	 * I[DLjava/lang/Object;I
	 */
	public String getInternal()
	{
		String res = "";
		for(int i = 0; i < types.size(); i++)
		{
			res += types.get(i).getInternalName();
		}
		return res;
	}
	
}
