package com.hexcore.cas.rulesystems;

import com.hexcore.cas.utilities.Log;

/**
 * Class RuleLoader

 * @authors Karl Zoller
 */

public class RuleLoader extends ClassLoader
{
	public static final String TAG = "RuleLoader";

	public Rule loadRule(byte[] b)
	{
		Rule rule = null;
		
		Log.debug(TAG, "Loading bytecode");
		Class<?> loadedClass = defineClass(null, b, 0, b.length);
		
		try
		{
			Object obj = loadedClass.getDeclaredConstructor().newInstance();
			if(obj instanceof Rule) 
				rule = (Rule)obj;
			else
				Log.error(TAG, "Invalid rule program, bytecode rule doesn't implement the Rule interface");
			
			Log.information(TAG, "Loaded rule: " + loadedClass.getName());
		}
		catch (Exception e)
		{
			Log.error(TAG, "Could not load rule: " + loadedClass.getName());
			e.printStackTrace();
		}
		
		return rule;
	}
}
