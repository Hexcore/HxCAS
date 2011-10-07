package com.hexcore.cas.rulesystems;

import java.util.ArrayList;


public class CALCompiler
{	
	public CALCompiler()
	{
		Parser.reset();
	}
	
	public void loadRules(String fileName)
	{
			Parser.reset();
			Scanner.Init(fileName);
			Errors.Init(fileName, "./", true);
			Parser.Parse();
			Errors.Summarize();
	}	
	
	public int getErrorCount()
	{
		return Parser.getErrorCount();
	}
	
	public ArrayList<String> getResult()
	{
		return Parser.getResult();
	}
	
	public byte[] getCode()
	{
		return Parser.getCode();
	}	
}
