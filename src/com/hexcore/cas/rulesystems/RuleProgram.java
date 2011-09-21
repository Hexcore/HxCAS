package com.hexcore.cas.rulesystems;

import com.hexcore.cas.model.Cell;
import com.hexcore.cas.utilities.Log;

public class RuleProgram extends ClassLoader
{
	public static final String TAG = "RuleProgram";
	
	Rule program;
	
	public boolean isValid()
	{
		return program != null;
	}
	
	public void loadBytecode(byte[] b)
	{
		Log.debug(TAG, "Loading bytecode");
		Class<?> loadedClass = defineClass(null, b, 0, b.length);
		
		try
		{
			Log.debug(TAG, "Creating instance of bytecode's class");
			Object obj = loadedClass.newInstance();
			if (obj instanceof Rule) 
				program = (Rule)obj;
			else
				Log.error(TAG, "Invalid rule program, bytecode rule doesn't implement the Rule interface");
		}
		catch (InstantiationException e)
		{
			program = null;
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			program = null;
			e.printStackTrace();
		}
	}
	
	public void run(Cell cell, Cell[] neighbours)
	{
		if (program == null)
		{
			Log.error(TAG, "Invalid rule program");
			return;
		}
		
		program.run(cell, neighbours);
	}
}
