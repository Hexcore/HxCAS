package com.hexcore.cas.rulesystems;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class CALCompiler

 * @authors Karl Zoller
 */

public class CALCompiler
{	
	public CALCompiler()
	{
		Parser.reset();
	}
	
	public void compile(String rules)
	{
		try
		{
			FileWriter writer = new FileWriter(new File("temp.cal"), false);
			writer.write(rules);
			writer.close();
			compileFile("temp.cal");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void compileFile(String fileName)
	{
		Parser.reset();
		Scanner.Init(fileName);
		Errors.Init(fileName, "./", true);
		Parser.Parse();
		Errors.Summarize();
	}
	
	public byte[] getCode()
	{
		return Parser.getCode();
	}
	
	public int getErrorCount()
	{
		return Parser.getErrorCount();
	}
	
	public ArrayList<String> getResult()
	{
		return Parser.getResult();
	}
}
