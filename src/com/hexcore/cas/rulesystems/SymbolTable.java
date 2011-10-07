package com.hexcore.cas.rulesystems;

import java.util.Stack;

public class SymbolTable
{
	private Stack<Scope>	scopeStack;
	
	public SymbolTable()
	{
		scopeStack = new Stack<SymbolTable.Scope>();
	}
	
	public void pushScope()
	{
		Scope newScope = new Scope();
		
		if(scopeStack.size() > 0)
			newScope.outer = scopeStack.peek();
		else
			newScope.outer = null;
		
		newScope.firstEntry = null;
		scopeStack.push(newScope);
	}
	
	public void popScope()
	{
		scopeStack.pop();
	}
	
	public TableEntry find(String name)
	{
		Scope currentScope = scopeStack.peek();
		
		while(currentScope != null)
		{
			TableEntry currentEntry = currentScope.firstEntry;
			
			while(currentEntry != null)
			{
				if(currentEntry.name.equals(name))
				{
					return currentEntry; 
				}
				currentEntry = currentEntry.nextEntry;
			}
			currentScope = currentScope.outer;
		}
		TableEntry noEntry = new TableEntry();
		noEntry.type = TableEntry.noType;
		return noEntry;
	}
	
	public void prepare()
	{
		TableEntry selfEntry = new TableEntry();
		selfEntry.type = TableEntry.cellType;
		selfEntry.kind = TableEntry.Cell;
		selfEntry.name = "self";
		selfEntry.offset = 0;
		insert(selfEntry);
		
		TableEntry typeEntry = new TableEntry();
		typeEntry.type = TableEntry.intType;
		typeEntry.kind = TableEntry.Property;
		typeEntry.name = "type";
		typeEntry.offset = 0;
		insert(typeEntry);
		
		TableEntry maxEntry = new TableEntry();
		maxEntry.type = TableEntry.doubleType;
		maxEntry.kind = TableEntry.aFunction;
		maxEntry.name = "max";
		insert(maxEntry);
		
		TableEntry minEntry = new TableEntry();
		minEntry.type = TableEntry.doubleType;
		minEntry.kind = TableEntry.aFunction;
		minEntry.name = "min";
		insert(minEntry);
		
		TableEntry logEntry = new TableEntry();
		logEntry.type = TableEntry.doubleType;
		logEntry.kind = TableEntry.sFunction;
		logEntry.name = "log";
		insert(logEntry);
		
		TableEntry lnEntry = new TableEntry();
		lnEntry.type = TableEntry.doubleType;
		lnEntry.kind = TableEntry.sFunction;
		lnEntry.name = "ln";
		insert(lnEntry);
		
		TableEntry sumEntry = new TableEntry();
		sumEntry.type = TableEntry.doubleType;
		sumEntry.kind = TableEntry.aFunction;
		sumEntry.name = "sum";
		insert(sumEntry);
		
		TableEntry countEntry = new TableEntry();
		countEntry.type = TableEntry.intType;
		countEntry.kind = TableEntry.aFunction;
		countEntry.name = "count";
		insert(countEntry);
		
		TableEntry neighboursEntry = new TableEntry();
		neighboursEntry.type = TableEntry.cellType + 1;
		neighboursEntry.kind = TableEntry.Cell;
		neighboursEntry.name = "neighbours";
		neighboursEntry.offset = 1;
		insert(neighboursEntry);
	}
	
	public void insert(TableEntry entry)
	{
		Scope currentScope = scopeStack.peek();
		entry.nextEntry = currentScope.firstEntry;
		currentScope.firstEntry = entry;
	}
	
	public class Scope
	{
		public Scope outer;
		public TableEntry firstEntry;
	}
	
	public String toString()
	{
		String str = "SymbolTable:\n";
		Scope currentScope = scopeStack.peek();
		
		while(currentScope != null)
		{
			str += "SCOPE: ";
			TableEntry currentEntry = currentScope.firstEntry;
			
			while(currentEntry != null)
			{
				str += currentEntry.name + "->" + currentEntry.type + " ";
				currentEntry = currentEntry.nextEntry;
			}
			str += "\n";
			currentScope = currentScope.outer;
		}
		return str;
	}
}
