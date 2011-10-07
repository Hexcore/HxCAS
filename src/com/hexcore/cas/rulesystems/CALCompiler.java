package com.hexcore.cas.rulesystems;


public class CALCompiler
{	
	public static void loadRules(String fileName)
	{
			Parser.reset();
			Scanner.Init(fileName);
			Errors.Init(fileName, "./", true);
			Parser.Parse();
			Errors.Summarize();
	}
	
	public byte[] getCode()
	{
		return null;
	}
	
}
